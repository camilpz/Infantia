package com.inf.daycare.dtos.get;

import lombok.Data;

@Data
public class GetDirectorDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private Boolean enabled;
}
