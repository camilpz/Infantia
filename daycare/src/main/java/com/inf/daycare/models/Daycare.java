package com.inf.daycare.models;

import com.inf.daycare.enums.TypeDaycareEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //Horarios de apertura y cierre
    private LocalTime openingTime;
    private LocalTime closingTime;

    //Periodo de inscripción global
    private LocalDate enrollmentPeriodStartDate;
    private LocalDate enrollmentPeriodEndDate;

    @Enumerated(EnumType.STRING)
    private TypeDaycareEnum type;

    private Boolean enabled;

    //Indica si la guardería ha sido validada por un administrador
    private Boolean validated = false;

    //Puede que una guardería no tenga latitud/longitud al inicio
    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    //-----------------------------------Relaciones------------------------------------

    @OneToMany(mappedBy = "daycare", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<DaycareShiftDefinition> shiftDefinitions = new HashSet<>();

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
        this.validated = false;
    }
}
