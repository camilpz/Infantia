package com.inf.daycare.dtos.post;

import com.inf.daycare.models.Title;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostDirectorDto {
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;

    private Set<Title> titles; // IDs de los títulos predefinidos seleccionados
    private String otherTitles;
}
