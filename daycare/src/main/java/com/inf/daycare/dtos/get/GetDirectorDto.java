package com.inf.daycare.dtos.get;

import lombok.Data;

import java.util.Set;

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

    private Set<GetTitleDto> titles; // Los títulos predefinidos del Director
    private String otherTitles;
}
