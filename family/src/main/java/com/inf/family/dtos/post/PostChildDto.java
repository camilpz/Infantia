package com.inf.family.dtos.post;

import com.inf.family.enums.GenderEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PostChildDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private GenderEnum gender;
    private String specialNeeds;
    private String dni;
}
