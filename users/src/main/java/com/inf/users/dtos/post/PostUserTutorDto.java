package com.inf.users.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserTutorDto extends PostUserDtoBase{
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
