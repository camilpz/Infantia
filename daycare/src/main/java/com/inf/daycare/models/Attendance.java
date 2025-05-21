package com.inf.daycare.models;

import com.inf.daycare.enums.AttendanceEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "attendance")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    //Un registro de asistencia también puede referenciar directamente a la guardería y al aula
    //Esto es útil para consultas rápidas, aunque ya están disponibles a través de Enrollment -> Classroom -> Daycare
    @ManyToOne
    @JoinColumn(name = "daycare_id", nullable = false)
    private Daycare daycare; // Relación directa con Daycare

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom; // Relación directa con Classroom

    // --- ID del niño (sigue siendo de otro microservicio) ---
    @Column(nullable = false)
    private Long childId; // Este ID aún viene del microservicio de niños

    // --- Datos de la Asistencia ---

    @Column(nullable = false)
    private LocalDate attendanceDate; // La fecha para la que se registra la asistencia

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceEnum status; // Estado de la asistencia: PRESENTE, AUSENTE, etc.

    @Column(nullable = false)
    private LocalDateTime checkInTime; // Hora de entrada

    private LocalDateTime checkOutTime; // Hora de salida (puede ser nulo inicialmente)

    private String notes; // Notas adicionales

    // --- Métodos de ciclo de vida de JPA ---
    @PrePersist
    public void prePersist() {
        if (this.checkInTime == null) {
            this.checkInTime = LocalDateTime.now();
        }
    }
}
