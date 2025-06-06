package com.inf.daycare.dtos.get;

import lombok.Data;

@Data
public class GetAuthorizedPersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String relationshipToChild;
    private String phoneNumber;
    private String email;
    private boolean isActive;
}
