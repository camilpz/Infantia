package com.inf.daycare.models;

import com.inf.daycare.enums.ShiftEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    private String name;
    private String description;
    private int ageMin;
    private int ageMax;
    private ShiftEnum shift;
    private int capacity;
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "daycare_id")
    private Daycare daycare;

    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities = new HashSet<>();

    //VER
    @OneToMany(mappedBy = "classroom", fetch = FetchType.LAZY)
    private Set<Enrollment> enrollments = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
