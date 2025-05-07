package com.inf.users.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class PostUserDtoBase {
    private String email;
    private String password;
    private String document;
    private Long documentType;
    private List<Long> roles;
    private List<PostContactDto> contacts;
}
