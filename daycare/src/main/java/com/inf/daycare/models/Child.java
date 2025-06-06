package com.inf.daycare.models;

import com.inf.daycare.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "child")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String dni;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String specialNeeds;
    private Boolean enabled = true;

    //Relación Many-to-Many con AuthorizedPerson
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // PERSIST y MERGE para manejar las asociaciones
    @JoinTable(
            name = "child_authorized_person",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "authorized_person_id")
    )
    private Set<AuthorizedPerson> authorizedPeople = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
