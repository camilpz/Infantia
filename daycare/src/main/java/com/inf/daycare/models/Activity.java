package com.inf.daycare.models;

import com.inf.daycare.enums.DayOfWeekEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Table(name = "activity")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ej: "Juego libre", "Desayuno", "Clase de Música"
    private String description; // Descripción opcional de la actividad

    @Enumerated(EnumType.STRING)
    private DayOfWeekEnum dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
