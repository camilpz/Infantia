package com.inf.users.dtos.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostContactDto {
    @NotNull(message = "El tipo de contacto es obligatorio")
    private Long contactTypeId;

    @NotNull(message = "El contenido es obligatorio")
    private String content;

    private Boolean isPrimary;
}
