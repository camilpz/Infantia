package com.inf.daycare.dtos.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUserDto {
    @NotNull(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotNull(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @NotNull(message = "El documento es obligatorio")
    private String document;

    @NotNull(message = "El tipo de documento es obligatorio")
    private Long documentType;

    @NotEmpty(message = "Debe tener al menos un rol")
    private List<Long> roles;

    @NotEmpty(message = "Debe tener al menos un contacto")
    private List<PostContactDto> contacts;

    @NotEmpty(message = "El nombre es obligatorio")
    private String firstName;

    @NotEmpty(message = "El apellido es obligatorio")
    private String lastName;

    @NotEmpty(message = "La dirección es obligatoria")
    private String address;

    @NotEmpty(message = "La ciudad es obligatoria")
    private String city;

    @NotEmpty(message = "El código postal es obligatorio")
    private String postalCode;

    //Especific fields for User

    //Director
    private List<Long> titles;

    //Tutor
    private String relationshipToChild;

    //Teacher
    //Nada en específico
}
