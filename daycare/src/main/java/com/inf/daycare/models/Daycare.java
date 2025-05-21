package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "daycare")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Daycare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
    private String email;
    private String location;
    private Boolean enabled;

    @OneToMany(mappedBy = "daycare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Classroom> classrooms;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    @OneToMany(mappedBy = "daycare")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "daycare")
    private List<Comment> comments;

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
