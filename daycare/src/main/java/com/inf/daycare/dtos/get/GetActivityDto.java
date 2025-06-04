package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.DayOfWeekEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class GetActivityDto {
    private Long id;
    private String name;
    private String description;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
