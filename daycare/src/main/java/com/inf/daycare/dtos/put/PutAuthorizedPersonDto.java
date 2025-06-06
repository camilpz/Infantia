package com.inf.daycare.dtos.put;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PutAuthorizedPersonDto {
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String firstName;

    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String lastName;

    @Size(max = 20, message = "El DNI no puede exceder los 20 caracteres")
    private String dni; // Si el DNI puede ser actualizado (cuidado con el unique=true)

    @Size(max = 100, message = "La relación no puede exceder los 100 caracteres")
    private String relationshipToChild;

    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres")
    private String phoneNumber;

    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;
}
