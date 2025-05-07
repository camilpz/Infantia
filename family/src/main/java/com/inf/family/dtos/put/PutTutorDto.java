package com.inf.family.dtos.put;

import lombok.Data;

@Data
public class PutTutorDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
