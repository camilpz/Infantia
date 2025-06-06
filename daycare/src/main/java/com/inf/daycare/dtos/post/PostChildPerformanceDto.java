package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.AchievementLevelEnum;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PostChildPerformanceDto {
    @NotNull(message = "El ID del niño no puede ser nulo")
    private Long childId;

    @NotNull(message = "El ID del docente no puede ser nulo")
    private Long teacherUserId; // El ID del usuario que es docente

    private LocalDate evaluationDate; // Opcional, si no se envía, se usa la fecha actual

    @NotNull(message = "El área de desarrollo no puede ser nula")
    private DevelopmentAreaEnum developmentArea;

    @NotBlank(message = "La habilidad/indicador no puede estar vacío")
    @Size(max = 500, message = "La habilidad/indicador no puede exceder los 500 caracteres")
    private String skillIndicator;

    @Size(max = 2000, message = "La observación no puede exceder los 2000 caracteres")
    private String observation;

    @NotNull(message = "El nivel de logro no puede ser nulo")
    private AchievementLevelEnum achievementLevel;

    @Size(max = 2000, message = "Las recomendaciones no pueden exceder los 2000 caracteres")
    private String recommendations;
}
