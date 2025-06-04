package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.DayOfWeekEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
public class PostActivityDto {
    private String name;
    private String description;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long classroomId;
}
