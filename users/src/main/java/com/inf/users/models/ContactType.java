package com.inf.users.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "contact_type")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
//
//    @OneToMany(mappedBy = "contactType", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
//    private List<Contact> contacts;
}
