package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PostEnrollmentDto {
    private Long childId;
    private Long daycareId;
    private LocalDate enrollmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ShiftEnum shift;
}
