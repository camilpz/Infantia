package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GetEnrollmentDto {
    private Long id;
    private LocalDate createdAt;
    private LocalDate enrollmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ShiftEnum shift;
    private StatusEnum status;
    private PayStatusEnum paymentStatus;
}
