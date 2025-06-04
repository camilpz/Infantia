package com.inf.daycare.models;

import com.inf.daycare.enums.ShiftEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "daycare_shift_definition",
        uniqueConstraints = @UniqueConstraint(columnNames = {"daycare_id", "shift_type"})) // Una guardería no puede tener dos definiciones para el mismo turno
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaycareShiftDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daycare_id", nullable = false)
    private Daycare daycare;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftEnum shiftType; // MAÑANA, TARDE, TURNO COMPLETO

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
