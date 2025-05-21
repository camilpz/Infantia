package com.inf.daycare.dtos.post;

import lombok.Data;

@Data
public class PostDaycareDto {
    private Long directorId;

    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
    private String email;
    private String location;
}
