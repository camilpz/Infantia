package com.inf.users.dtos.get;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUserDto {
    private Long id;
    private String email;
    private String document;
    private String documentType;
    private List<String> rolesNames;
}
