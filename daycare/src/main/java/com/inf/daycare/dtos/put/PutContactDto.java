package com.inf.daycare.dtos.put;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutContactDto {
    private Long id;
    private Long contactTypeId;
    private String content;
    private Boolean isPrimary;
}
