package com.inf.daycare.dtos.put;

import com.inf.daycare.enums.AttendanceEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PutAttendanceDto {
    private Long id;
    private AttendanceEnum status;
    private LocalDateTime checkOutTime;
    private String notes;
}
