package com.inf.users.dtos;

import com.inf.users.models.Contact;
import lombok.Data;

import java.util.List;

@Data
public class PostUserDto {
    private String email;
    private String password;
    private String document;
    private Long documentType;
    private Boolean enabled;
    private List<Long> roles;
    private List<PostContactDto> contacts;
}
