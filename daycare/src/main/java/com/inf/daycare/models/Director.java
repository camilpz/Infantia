package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY) // Lazy loading es bueno para no cargar todos los títulos siempre
    @JoinTable(
            name = "director_titles", // Tabla intermedia
            joinColumns = @JoinColumn(name = "director_id"),
            inverseJoinColumns = @JoinColumn(name = "title_id")
    )
    private Set<Title> titles;

    @OneToMany(mappedBy = "director")
    private List<Daycare> daycares;

    private String otherTitles; // Otros títulos que no están en la tabla de títulos

    @PrePersist
    public void prePersist() {
        this.enabled = true;
    }
}
