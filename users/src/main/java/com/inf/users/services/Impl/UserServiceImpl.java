package com.inf.users.services.Impl;

import com.inf.users.clients.FamilyClient;
import com.inf.users.clients.requests.PostTutorRequest;
import com.inf.users.clients.requests.PutTutorRequest;
import com.inf.users.dtos.get.GetUserDto;
import com.inf.users.dtos.post.LoginDto;
import com.inf.users.dtos.post.PostContactDto;
import com.inf.users.dtos.post.PostUserTutorDto;
import com.inf.users.dtos.put.EditUserDtoBase;
import com.inf.users.dtos.put.PutContactDto;
import com.inf.users.dtos.put.PutUserTutorDto;
import com.inf.users.mapper.UserMapper;
import com.inf.users.models.*;
import com.inf.users.repositories.*;
import com.inf.users.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final UserMapper userMapper;

    private final FamilyClient familyClient;

    @Override
    @Transactional
    public GetUserDto createUserTutor(PostUserTutorDto userDTO) {
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            throw new IllegalArgumentException("El usuario no posee roles");
        }

        DocumentType documentType = getDocumentTypeOrThrow(userDTO.getDocumentType());

        List<Contact> contacts = userDTO.getContacts().stream()
                .map(contactDto -> {
                    ContactType contactType = getContactTypeOrThrow(contactDto.getContactTypeId());
                    return Contact.builder()
                            .contactType(contactType)
                            .content(contactDto.getContent())
                            .isPrimary(contactDto.getIsPrimary())
                            .build();
                })
                .collect(Collectors.toList());

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .document(userDTO.getDocument())
                .documentType(documentType)
                .roles(getRoles(userDTO))
                .contacts(contacts)
                .build();

        contacts.forEach(contact -> contact.setUser(user));

        //Guardo el usuario en la base de datos
        userRepository.save(user);

        //Por cada rol creo un usuario de ese tipo
//        user.getRoles().forEach(role -> {
//            if(role.getName().equals("TUTOR")) {
//                PostTutorRequest postTutorRequest = PostTutorRequest.builder()
//                        .userId(user.getId())
//                        .firstName(userDTO.getFirstName())
//                        .lastName(userDTO.getLastName())
//                        .address(userDTO.getAddress())
//                        .postalCode(userDTO.getPostalCode())
//                        .relationshipToChild(userDTO.getRelationshipToChild())
//                        .city(userDTO.getCity())
//                        .build();
        //          PostTutorRequest postTutorRequest = userMapper.postUserDtoToPostTutorRequest(userDTO);
//
//                try {
//                    familyClient.createTutor(postTutorRequest);
//                } catch (Exception e) {
//                    throw new IllegalStateException("No se pudo crear el tutor en el microservicio de Familia", e);
//                }
//
//            }
//            else if(role.getName().equals("DIRECTOR")) {
//                //TODO: Crear el director
//            }
//        });

        return userMapper.userToGetUserDto(user);
    }

    //Busco los roles por id y los devuelvo en un Set
    private Set<Role> getRoles(PostUserTutorDto userDTO) {
        return userDTO.getRoles().stream()
                .map(roleId -> roleRepostitory.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id: " + roleId + " no existe")))
                .collect(Collectors.toSet());
    }

    @Override
    public GetUserDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No se encontro un usuario con ese email"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        return userMapper.userToGetUserDto(user);
    }

    @Override
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
    public GetUserDto editTutor(Long id, PutUserTutorDto putUserTutorDto) {
        User user = getUserOrThrow(id);

        // Actualizar datos básicos
        DocumentType documentType = getDocumentTypeOrThrow(putUserTutorDto.getDocumentType());
        user.setDocument(putUserTutorDto.getDocument());
        user.setDocumentType(documentType);

        // Mapeo de contactos existentes
        Map<Long, Contact> existingContacts = user.getContacts().stream()
                .filter(c -> c.getId() != null)
                .collect(Collectors.toMap(Contact::getId, c -> c));

        List<Contact> contactsToKeep = new ArrayList<>();

        // Actualizar o crear contactos
        for (PutContactDto dto : putUserTutorDto.getContacts()) {
            Contact contact = updateOrCreateContact(dto, user, existingContacts);
            contactsToKeep.add(contact);
        }

        // Eliminar los contactos que ya no están
        for (Contact contact : existingContacts.values()) {
            user.getContacts().remove(contact);
        }

        // Agregar los nuevos o actualizados
        for (Contact contact : contactsToKeep) {
            if (!user.getContacts().contains(contact)) {
                user.getContacts().add(contact);
            }
        }

        // Guardar cambios
        userRepository.save(user);

        return userMapper.userToGetUserDto(user);
    }

    private Contact updateOrCreateContact(PutContactDto dto, User user, Map<Long, Contact> existingContacts) {
        Contact contact;

        if (dto.getId() != null && existingContacts.containsKey(dto.getId())) {
            // Si el contacto ya existe, lo actualizamos
            contact = existingContacts.get(dto.getId());
            contact.setContent(dto.getContent());
            contact.setIsPrimary(dto.getIsPrimary());
            contact.setContactType(getContactTypeOrThrow(dto.getContactTypeId()));
            existingContacts.remove(dto.getId());  // Lo quitamos del map
        } else {
            // Si es un contacto nuevo, lo creamos
            contact = Contact.builder()
                    .content(dto.getContent())
                    .isPrimary(dto.getIsPrimary())
                    .contactType(getContactTypeOrThrow(dto.getContactTypeId()))
                    .user(user)
                    .build();
        }

        return contact;
    }

    //------------------------------------------------------------Métodos para validar--------------------------------------------------------------------

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
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
