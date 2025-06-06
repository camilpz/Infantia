package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "authorized_person")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String dni;

    @Column(nullable = false)
    private String relationshipToChild; // Ej: "Madre", "Padre", "Abuela", "Tío", "Niñera", "Amigo"

    private String phoneNumber;
    private String email;

    private boolean isActive;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
    }
}
