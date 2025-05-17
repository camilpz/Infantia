package com.inf.users.dtos.put;

import com.inf.users.dtos.post.PostContactDto;
import lombok.Data;

import java.util.List;

@Data
public abstract class EditUserDtoBase {
    private String document;
    private Long documentType;
    private List<PutContactDto> contacts;

    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
}
