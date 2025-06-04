package com.inf.daycare.dtos.put;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PutUserDto {
    private String document;
    private Long documentType;
    private List<PutContactDto> contacts;
}
