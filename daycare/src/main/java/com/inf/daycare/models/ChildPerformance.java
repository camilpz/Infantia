package com.inf.daycare.models;

import com.inf.daycare.enums.AchievementLevelEnum;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "child_performance")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_user_id", nullable = false) //Asumiendo que el docente es un usuario
    private User teacher;

    @Column(nullable = false)
    private LocalDate evaluationDate; //Fecha de la evaluación

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DevelopmentAreaEnum developmentArea;

    @Column(nullable = false)
    private String skillIndicator; //Habilidad o indicador específico por ejemplo "Identifica colores primarios")

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementLevelEnum achievementLevel; //Nivel de logro (Ej: Logrado, En Progreso)

    @Column(columnDefinition = "TEXT")
    private String recommendations;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.evaluationDate == null) { //Si no se proporciona fecha de evaluación, usar la actual
            this.evaluationDate = LocalDate.now();
        }
    }
}
