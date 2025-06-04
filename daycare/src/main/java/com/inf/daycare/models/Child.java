package com.inf.daycare.models;

import com.inf.daycare.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
