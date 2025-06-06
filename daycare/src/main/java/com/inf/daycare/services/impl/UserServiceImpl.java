package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetUserDto;
import com.inf.daycare.dtos.post.*;
import com.inf.daycare.dtos.put.PutContactDto;
import com.inf.daycare.dtos.put.PutUserDto;
import com.inf.daycare.mapper.UserMapper;
import com.inf.daycare.models.*;
import com.inf.daycare.repositories.*;
import com.inf.daycare.services.DirectorService;
import com.inf.daycare.services.TeacherService;
import com.inf.daycare.services.TutorService;
import com.inf.daycare.services.UserService;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepostitory roleRepostitory;
    private final ContactRepository contactRepository;
    private final ContactTypeRepository contactTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentTypeRepository documentTypeRepository;
    private final TitleRepository titleRepository;
    private final UserMapper userMapper;

    //Services
    private final TutorService tutorService;
    private final TeacherService teacherService;
    private final DirectorService directorService;

    @Override
    @Transactional
    public GetUserDto createUser(PostUserDto postUserDto) {
        if (postUserDto.getRoles() == null || postUserDto.getRoles().isEmpty()) {
            throw new IllegalArgumentException("El usuario no posee roles");
        }

        DocumentType documentType = getDocumentTypeOrThrow(postUserDto.getDocumentType());

        List<Contact> contacts = postUserDto.getContacts().stream()
                .map(contactDto -> {
                    ContactType contactType = getContactTypeOrThrow(contactDto.getContactTypeId());
                    return Contact.builder()
                            .contactType(contactType)
                            .content(contactDto.getContent())
                            .isPrimary(contactDto.getIsPrimary())
                            .build();
                })
                .toList();

        User user = User.builder()
                .email(postUserDto.getEmail())
                .password(passwordEncoder.encode(postUserDto.getPassword()))
                .document(postUserDto.getDocument())
                .documentType(documentType)
                .contacts(new ArrayList<>(contacts))
                .roles(postUserDto.getRoles().stream()
                    .map(roleId -> roleRepostitory.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + roleId)))
                    .collect(Collectors.toSet()))
                .build();

        contacts.forEach(contact -> contact.setUser(user));

        //Guardo el usuario en la base de datos
        userRepository.save(user);

        //De acuerdo al rol del usuario, se crea el perfil correspondiente
        user.getRoles().forEach(role -> {
            if (role.getName().equals("TUTOR")) {
                PostTutorDto postTutorDto = userMapper.getPostUserDtotoToPostTutorDto(postUserDto);

                //Crear el tutor y asociarlo al usuario
                Tutor tutor = tutorService.create(postTutorDto, user);
                System.out.println("ID DE USUARIO:" + tutor.getUser().getId());
                user.setTutorProfile(tutor);

            } else if (role.getName().equals("DIRECTOR")) {
                Set<Title> titles = postUserDto.getTitles().stream()
                        .map(this::getTitleOrThrow)
                        .collect(Collectors.toSet());

                PostDirectorDto postDirectorDto = userMapper.getPostUserDtotoToPostDirectorDto(postUserDto);
                postDirectorDto.setTitles(titles);

                //Crear el director y asociarlo al usuario
                directorService.create(postDirectorDto, user);

            } else if (role.getName().equals("MAESTRO")) {
                PostTeacherDto postTeacherDto = userMapper.getPostUserDtotoToPostTeacherDto(postUserDto);

                //Crear el teacher y asociarlo al usuario
                Teacher teacher = teacherService.create(postTeacherDto, user);
                user.setTeacherProfile(teacher);

            } else {
                //Rol incorrecto o admin
                throw new IllegalArgumentException("Rol no soportado: " + role.getName());
            }
        });

        //Guardo el usuario en la base de datos
        userRepository.save(user);

        return userMapper.userToGetUserDto(user);
    }

    @Override
    @Transactional
    public List<GetUserDto> getAllUsers() {
        var users = userRepository.findAll();

        return userMapper.userListToGetUserDtoList(users);
    }

    @Override
    public GetUserDto getUserById(Long id) {
        var user = getUserOrThrow(id);

        return userMapper.userToGetUserDto(user);
    }

    @Override
    @Transactional
    public GetUserDto editUser(Long id, PutUserDto putUserDto) {
        User user = getUserOrThrow(id);
        DocumentType documentType = getDocumentTypeOrThrow(putUserDto.getDocumentType());

        user.setDocument(putUserDto.getDocument());
        user.setDocumentType(documentType);

        // Crea un mapa de contactos existentes para búsqueda rápida
        Map<Long, Contact> existingContactsMap = user.getContacts().stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(Contact::getId, c -> c));

        // Prepara una nueva lista para los contactos finales del usuario
        List<Contact> updatedOrNewContacts = new ArrayList<>();

        // Itera sobre los contactos del DTO
        for (PutContactDto dto : putUserDto.getContacts()) {
            Contact contact;
            if (dto.getId() != null && dto.getId() != 0 && existingContactsMap.containsKey(dto.getId())) {
                // Si el ID existe y es válido (no nulo ni 0), actualiza el contacto existente
                contact = existingContactsMap.get(dto.getId());
                contact.setContent(dto.getContent());
                contact.setIsPrimary(dto.getIsPrimary());
                contact.setContactType(getContactTypeOrThrow(dto.getContactTypeId()));
            } else {
                // Si el ID es nulo o 0, es un nuevo contacto
                contact = Contact.builder()
                        .content(dto.getContent())
                        .isPrimary(dto.getIsPrimary())
                        .contactType(getContactTypeOrThrow(dto.getContactTypeId()))
                        .user(user) // Asigna el usuario al nuevo contacto
                        .build();
            }
            updatedOrNewContacts.add(contact); // Agrega el contacto (actualizado o nuevo) a la lista temporal
        }

        // Manejo de eliminación: Los contactos en la colección original del usuario
        // que NO están en updatedOrNewContacts serán eliminados automáticamente
        // gracias a `orphanRemoval = true` cuando se limpie la colección y se reasigne.

        // Limpia la colección de contactos del usuario (esto disparará orphanRemoval)
        user.getContacts().clear();
        // Añade los contactos actualizados y nuevos a la colección del usuario
        user.getContacts().addAll(updatedOrNewContacts);

        // Guardar el usuario: Esto guardará/actualizará los contactos por cascada y eliminará los huérfanos.
        userRepository.save(user);

        return userMapper.userToGetUserDto(user);
    }

    //------------------------------------------------------------Métodos para validar--------------------------------------------------------------------

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    public Boolean emailIsAvailable(String email) {
        return userRepository.existsByEmail(email);
    }

    private Title getTitleOrThrow(Long id) {
        return titleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Título inválido"));
    }

    private DocumentType getDocumentTypeOrThrow(Long id) {
        return documentTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de documento inválido"));
    }

    private ContactType getContactTypeOrThrow(Long id) {
        return contactTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de contacto inválido"));
    }
    
}
