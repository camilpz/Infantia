package com.inf.users.dtos.post;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserDirectorDto extends PostUserDtoBase{
    @NotNull(message = "Debe incluir al menos un título")
    private String titles;

    //private String country;
}
