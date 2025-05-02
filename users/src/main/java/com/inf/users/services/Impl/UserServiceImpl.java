package com.inf.users.services.Impl;

import com.inf.users.dtos.GetUserDto;
import com.inf.users.dtos.PostUserDto;
import com.inf.users.models.Contact;
import com.inf.users.models.ContactType;
import com.inf.users.models.Role;
import com.inf.users.models.User;
import com.inf.users.repositories.ContactRepository;
import com.inf.users.repositories.ContactTypeRepository;
import com.inf.users.repositories.RoleRepostitory;
import com.inf.users.repositories.UserRepository;
import com.inf.users.services.UserService;
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

    @Override
    @Transactional
    public GetUserDto create(PostUserDto userDTO) {
        if(userDTO.getRoles() == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        List<Contact> contacts = new ArrayList<>();

        userDTO.getContacts().forEach(contact -> {
            ContactType contactType = contactTypeRepository.findById(contact.getContactTypeId()).orElseThrow(
                    () -> new IllegalArgumentException("Contact Type not found"));

            Contact cont = Contact.builder()
                    .contactType(contactType)
                    .content(contact.getContent())
                    .isPrimary(contact.getIsPrimary())
                    .build();

            contacts.add(cont);
        });


        User user = User.builder()
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(getRoles(userDTO))
                .contacts(contacts)
                .build();

        try {
            userRepository.save(user);
            return GetUserDto.builder()
                    .email(user.getEmail())
                    .document(user.getDocument())
                    .roles(user.getRoles().stream().map(Role::getId).collect(Collectors.toSet()))
                    .contacts(user.getContacts().stream().map(contact -> contact.getId()).collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

//    private void verifyRolesExists(PostUserDto userDTO) {
//        userDTO.getRoles().forEach(role -> {
//            if (!roleRepostitory.existsById(role)) {
//                throw new IllegalArgumentException("Role with ID " + role + " does not exist");
//            }
//        });
//    }

    private Set<Role> getRoles(PostUserDto userDTO) {
        return userDTO.getRoles().stream()
                .map(roleId -> roleRepostitory.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Role with ID " + roleId + " does not exist")))
                .collect(Collectors.toSet());
    }
    
}
