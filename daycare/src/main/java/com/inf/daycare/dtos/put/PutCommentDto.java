package com.inf.daycare.dtos.put;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PutCommentDto {
    @Min(value = 1, message = "El valor no puede ser menor a 1")
    @Max(value = 5, message = "El valor no puede ser mayor a 5")
    private Integer rating;

    @NotBlank(message = "El contenido no debe estar vacío")
    private String content;
}
