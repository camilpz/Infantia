package com.inf.daycare.dtos.get;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetContactDto {
    private Long id;
    private String content;
    private Boolean isPrimary;
    private String contactTypeName;
}
