package com.inf.users.dtos;

import jakarta.persistence.Column;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
public class GetUserDto {
    private String email;
    private String password;
    private String document;
    private Long documentType;
    private List<Long> roleId;
}
