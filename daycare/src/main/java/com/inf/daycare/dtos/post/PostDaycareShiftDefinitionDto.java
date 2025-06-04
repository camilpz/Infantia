package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.ShiftEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class PostDaycareShiftDefinitionDto {
    @NotNull(message = "El tipo de turno es obligatorio.")
    private ShiftEnum shiftType;

    @NotNull(message = "La hora de inicio es obligatoria.")
    private LocalTime startTime;

    @NotNull(message = "La hora de fin es obligatoria.")
    private LocalTime endTime;
}
