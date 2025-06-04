package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.ShiftEnum;
import lombok.Data;

import java.util.List;

@Data
public class GetClassroomDto {
    private Long id;
    private String name;
    private String description;
    private int ageMin;
    private int ageMax;
    private ShiftEnum shift;
    private int capacity;
    private Boolean enabled;
    private List<GetActivityDto> activities;
}
