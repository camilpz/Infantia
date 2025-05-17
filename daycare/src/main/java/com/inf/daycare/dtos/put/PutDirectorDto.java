package com.inf.daycare.dtos.put;

import lombok.Data;

@Data
public class PutDirectorDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private Boolean enabled;
}
