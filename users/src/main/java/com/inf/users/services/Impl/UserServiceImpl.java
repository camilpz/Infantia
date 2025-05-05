package com.inf.users.services.Impl;

import com.inf.users.dtos.*;
import com.inf.users.models.*;
import com.inf.users.repositories.*;
import com.inf.users.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    @Transactional
    public GetUserDto create(PostUserDto userDTO) {
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

        userRepository.save(user);

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
    private Set<Role> getRoles(PostUserDto userDTO) {
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
    public GetUserDto editUser(Long id, EditUserDto editUserDto) {
        User user = getUserOrThrow(id);

        //Actualizar Documento y Tipo de Documento
        DocumentType documentType = getDocumentTypeOrThrow(editUserDto.getDocumentType());

        user.setDocument(editUserDto.getDocument());
        user.setDocumentType(documentType);

        //Actualizar Contactos
        List<Contact> updatedContacts = editUserDto.getContacts().stream()
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

        user.setContacts(updatedContacts);

        userRepository.save(user);

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

    //Métodos para validar

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
