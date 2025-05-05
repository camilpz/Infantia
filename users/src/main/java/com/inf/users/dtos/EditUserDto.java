package com.inf.users.dtos;

import lombok.Data;

import java.util.List;

@Data
public class EditUserDto {
    private String document;
    private Long documentType;
    private List<PostContactDto> contacts;
}
