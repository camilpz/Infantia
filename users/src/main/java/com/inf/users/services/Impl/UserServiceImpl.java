package com.inf.users.services.Impl;

import com.inf.users.clients.FamilyClient;
import com.inf.users.clients.requests.PostTutorRequest;
import com.inf.users.clients.requests.PutTutorRequest;
import com.inf.users.dtos.get.GetUserDto;
import com.inf.users.dtos.post.LoginDto;
import com.inf.users.dtos.post.PostUserTutorDto;
import com.inf.users.dtos.put.EditUserDtoBase;
import com.inf.users.dtos.put.PutContactDto;
import com.inf.users.dtos.put.PutUserTutorDto;
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
        user.getRoles().forEach(role -> {
            if(role.getName().equals("TUTOR")) {
                PostTutorRequest postTutorRequest = PostTutorRequest.builder()
                        .userId(user.getId())
                        .firstName(userDTO.getFirstName())
                        .lastName(userDTO.getLastName())
                        .address(userDTO.getAddress())
                        .postalCode(userDTO.getPostalCode())
                        .relationshipToChild(userDTO.getRelationshipToChild())
                        .city(userDTO.getCity())
                        .build();

                try {
                    familyClient.createTutor(postTutorRequest);
                } catch (Exception e) {
                    throw new IllegalStateException("No se pudo crear el tutor en el microservicio de Familia", e);
                }

            }
            else if(role.getName().equals("DIRECTOR")) {
                //TODO: Crear el director
            }
        });


        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .document(user.getDocument())
                .rolesNames(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .build();
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

        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .document(user.getDocument())
                .documentType(user.getDocumentType().getName())
                .rolesNames(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<GetUserDto> getAllUsers() {
        var users = userRepository.findAll();

        return users.stream()
                .map(user -> GetUserDto.builder()
                        .email(user.getEmail())
                        .document(user.getDocument())
                        .documentType(user.getDocumentType().getName())
                        .rolesNames(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public GetUserDto getUserById(Long id) {
        var user = getUserOrThrow(id);

        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .document(user.getDocument())
                .documentType(user.getDocumentType().getName())
                .rolesNames(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public GetUserDto editTutor(Long id, PutUserTutorDto putUserTutorDto) {
        User user = getUserOrThrow(id);

        // Actualizar Documento y Tipo de Documento
        DocumentType documentType = getDocumentTypeOrThrow(putUserTutorDto.getDocumentType());
        user.setDocument(putUserTutorDto.getDocument());
        user.setDocumentType(documentType);

        // Sin borrar contactos, simplemente actualizarlos
        List<Contact> currentContacts = new ArrayList<>(user.getContacts());

        // Crear nuevos contactos
        List<Contact> newContacts = putUserTutorDto.getContacts().stream()
                .map(dto -> {
                    ContactType contactType = getContactTypeOrThrow(dto.getContactTypeId());

                    return Contact.builder()
                            .contactType(contactType)
                            .content(dto.getContent())
                            .isPrimary(dto.getIsPrimary())
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());

        // Compara y elimina contactos no referenciados
        List<Contact> contactsToRemove = new ArrayList<>(currentContacts);
        contactsToRemove.removeAll(newContacts);

        user.getContacts().removeAll(contactsToRemove);
        user.getContacts().addAll(newContacts);

        // Guardar cambios en User
        userRepository.save(user);

        try {
            PutTutorRequest putTutorRequest = PutTutorRequest.builder()
                    .firstName(putUserTutorDto.getFirstName())
                    .lastName(putUserTutorDto.getLastName())
                    .address(putUserTutorDto.getAddress())
                    .postalCode(putUserTutorDto.getPostalCode())
                    .relationshipToChild(putUserTutorDto.getRelationshipToChild())
                    .city(putUserTutorDto.getCity())
                    .build();

            familyClient.updateTutor(user.getId(), putTutorRequest);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo editar el tutor en el microservicio de Familia", e);
        }

        return GetUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .document(user.getDocument())
                .documentType(user.getDocumentType().getName())
                .rolesNames(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .build();
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
