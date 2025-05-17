package com.inf.users.dtos.put;

import com.inf.users.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PutUserTutorDto extends EditUserDtoBase{
    private String relationshipToChild;
}
