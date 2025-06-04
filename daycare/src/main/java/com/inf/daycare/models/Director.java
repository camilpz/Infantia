package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "director")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private Boolean enabled;

    @OneToMany(mappedBy = "director")
    private List<Daycare> daycares;

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
