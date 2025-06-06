package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.AchievementLevelEnum;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GetChildPerformanceDto {
    private Long id;
    private Long childId;
    private String childFirstName;
    private String childLastName;
    private Long teacherUserId;
    private String teacherFirstName; // Nombre del docente
    private String teacherLastName;  // Apellido del docente
    private LocalDate evaluationDate;
    private DevelopmentAreaEnum developmentArea;
    private String developmentAreaDisplayName; // Para mostrar el nombre amigable del enum
    private String skillIndicator;
    private String observation;
    private AchievementLevelEnum achievementLevel;
    private String achievementLevelDisplayName; // Para mostrar el nombre amigable del enum
    private String recommendations;
    private LocalDateTime createdAt;
}
