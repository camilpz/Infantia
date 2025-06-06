package com.inf.daycare.dtos.put;

import lombok.Data;

import java.util.Set;

@Data
public class PutDirectorDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private Boolean enabled;
    // Nuevos campos para los títulos del Director
    private Set<Long> titleIds; // IDs de los títulos predefinidos seleccionados
    private String otherTitles;
}
