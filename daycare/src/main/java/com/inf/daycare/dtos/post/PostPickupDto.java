package com.inf.daycare.dtos.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostPickupDto {
    @NotNull(message = "El ID del niño no puede ser nulo")
    private Long childId;

    @NotNull(message = "El ID de la persona autorizada no puede ser nulo")
    private Long authorizedPersonId;

    @NotNull(message = "El ID de la asistencia no puede ser nulo")
    private Long attendanceId;

    private LocalDateTime pickupTime;
    private String notes;
}
