package com.inf.daycare.dtos.get;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTutorDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private Boolean enabled;
    private String city;
}
