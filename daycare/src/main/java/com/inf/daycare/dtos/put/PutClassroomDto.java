package com.inf.daycare.dtos.put;

import com.inf.daycare.enums.ShiftEnum;
import lombok.Data;

@Data
public class PutClassroomDto {
    private String name;
    private String description;
    private int ageMin;
    private int ageMax;
    private ShiftEnum shift;
    private int capacity;
}
