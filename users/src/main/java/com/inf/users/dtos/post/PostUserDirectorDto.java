package com.inf.users.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserDirectorDto extends PostUserDtoBase{
    private String directorName;
    private String directorLastName;
    private String directorDocumentNumber;
    private String directorDocumentType;
    private String directorPhoneNumber;
    private String directorEmail;
    private String directorAddress;
    private String directorCity;
    private String directorCountry;
}
