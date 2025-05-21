package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.AttendanceEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PostAttendanceDto {
    @NotNull(message = "El ID del niño no puede ser nulo")
    private Long childId;

    @NotNull(message = "El ID de la guardería no puede ser nulo")
    private Long daycareId;

    @NotNull(message = "El ID de la inscripción no puede ser nulo")
    private Long enrollmentId;

    @NotNull(message = "La fecha de asistencia no puede ser nula")
    private LocalDate attendanceDate;

    @NotNull(message = "El estado de la asistencia no puede ser nulo")
    private AttendanceEnum status;

    private String notes;
}
