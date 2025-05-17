package com.inf.daycare.dtos.get;

import lombok.Data;

@Data
public class GetTeacherDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String dni;
    private String address;
    private String postalCode;
    private String city;
    private Boolean enabled;
}
