package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "pickup_record")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pickup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child; // Directamente al niño

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picked_up_by_id", nullable = false)
    private AuthorizedPerson pickedUpBy;

    @Column(nullable = false)
    private LocalDateTime pickupTime;

    private String notes;

    // Relación con la asistencia, si es que se registra un retiro
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", unique = true, nullable = false)
    private Attendance attendance;

    //AÑADIR RELACIÓN CON USUARIO QUE REGISTRA EL RETIRO?
    //@ManyToOne @JoinColumn(name = "registered_by_user_id")

    @PrePersist
    public void prePersist() {
        if (this.pickupTime == null) {
            this.pickupTime = LocalDateTime.now();
        }
    }
}
