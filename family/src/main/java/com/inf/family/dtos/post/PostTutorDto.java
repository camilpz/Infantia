package com.inf.family.dtos.post;

import lombok.Data;

@Data
public class PostTutorDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
