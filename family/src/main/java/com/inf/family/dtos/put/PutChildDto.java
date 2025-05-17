package com.inf.family.dtos.put;

import com.inf.family.enums.GenderEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PutChildDto {
    private String firstName;
    private String lastName;
    private String dni;
    private LocalDate birthDate;
    private GenderEnum gender;
    private String specialNeeds;
}
