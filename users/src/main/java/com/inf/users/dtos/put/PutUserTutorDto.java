package com.inf.users.dtos.put;

import com.inf.users.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutUserTutorDto extends EditUserDtoBase{
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String relationshipToChild;
    private String city;
}
