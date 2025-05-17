package com.inf.daycare.dtos.post;

import lombok.Data;

@Data
public class PostDirectorDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
}
