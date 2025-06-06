package com.inf.daycare.dtos.put;

import com.inf.daycare.enums.AchievementLevelEnum;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PutChildPerformaneDto {
    private LocalDate evaluationDate;

    private DevelopmentAreaEnum developmentArea;

    @Size(max = 500, message = "La habilidad/indicador no puede exceder los 500 caracteres")
    private String skillIndicator;

    @Size(max = 2000, message = "La observación no puede exceder los 2000 caracteres")
    private String observation;

    private AchievementLevelEnum achievementLevel;

    @Size(max = 2000, message = "Las recomendaciones no pueden exceder los 2000 caracteres")
    private String recommendations;
}
