package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.ShiftEnum;
import lombok.Data;

@Data
public class PostClassroomDto {
    private String name;
    private String description;
    private int ageMin;
    private int ageMax;
    private ShiftEnum shift;
    private int capacity;
    private Long daycareId;
}
