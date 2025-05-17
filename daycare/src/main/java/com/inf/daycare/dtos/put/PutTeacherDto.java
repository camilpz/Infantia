package com.inf.daycare.dtos.put;

import lombok.Data;

@Data
public class PutTeacherDto {
    private String firstName;
    private String lastName;
    private String dni;
    private String address;
    private String postalCode;
    private String city;
}
