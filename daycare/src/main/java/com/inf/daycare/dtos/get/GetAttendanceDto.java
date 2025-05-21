package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.AttendanceEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GetAttendanceDto {
    private Long id;
    private Long childId;
    private LocalDate attendanceDate;
    private AttendanceEnum status;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String notes;
}
