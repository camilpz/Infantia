package com.inf.daycare.dtos.put;

import lombok.Data;

@Data
public class PutDaycareDto {
    private Long directorId;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
    private String email;
}
