package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "classroom")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relacion
    private Long childId;

    private String name;
    private String description;
    private String ageRange;
    private String schedule;
    private String location;
    private Boolean enabled;

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
