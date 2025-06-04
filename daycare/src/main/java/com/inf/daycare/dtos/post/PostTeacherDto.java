package com.inf.daycare.dtos.post;

import lombok.Data;

@Data
public class PostTeacherDto {
    private String firstName;
    private String lastName;
    private String dni;
    private String address;
    private String postalCode;
    private String city;
}
