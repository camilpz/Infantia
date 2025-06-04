package com.inf.daycare.dtos.post;

import lombok.Data;

@Data
public class PostTutorDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
