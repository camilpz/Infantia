package com.inf.users.dtos;

import lombok.Data;

@Data
public class PostContactDto {
    private Long contactTypeId;
    private String content;
    private Boolean isPrimary;
}
