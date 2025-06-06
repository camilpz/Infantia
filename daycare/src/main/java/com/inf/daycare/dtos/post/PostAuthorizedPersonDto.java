package com.inf.daycare.dtos.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostAuthorizedPersonDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String lastName;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 20, message = "El DNI no puede exceder los 20 caracteres")
    private String dni;

    @NotBlank(message = "La relación con el niño no puede estar vacía")
    @Size(max = 100, message = "La relación no puede exceder los 100 caracteres")
    private String relationshipToChild;

    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres")
    private String phoneNumber;

    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;
}
