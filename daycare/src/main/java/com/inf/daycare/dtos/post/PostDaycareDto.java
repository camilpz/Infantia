package com.inf.daycare.dtos.post;

import com.inf.daycare.enums.TypeDaycareEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PostDaycareDto {
    @NotBlank(message = "El nombre de la guardería es obligatorio.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    private String name;

    @NotBlank(message = "La dirección es obligatoria.")
    @Size(min = 5, max = 255, message = "La dirección debe tener entre 5 y 255 caracteres.")
    private String address;

    @NotBlank(message = "El código postal es obligatorio.")
    @Size(max = 10, message = "El código postal no puede exceder los 10 caracteres.")
    private String postalCode;

    @NotBlank(message = "La ciudad es obligatoria.")
    @Size(max = 100, message = "La ciudad no puede exceder los 100 caracteres.")
    private String city;

    @NotBlank(message = "El estado es obligatorio.")
    @Size(max = 100, message = "El estado no puede exceder los 100 caracteres.")
    private String state;

    @NotBlank(message = "El país es obligatorio.")
    @Size(max = 100, message = "El país no puede exceder los 100 caracteres.")
    private String country;

    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Pattern(regexp = "\\d{10,15}", message = "El número de teléfono debe contener solo dígitos y tener entre 10 y 15 caracteres.")
    private String phoneNumber; // Usando el nombre que me proporcionaste

    @Email(message = "El formato del email no es válido.")
    @NotBlank(message = "El email es obligatorio.")
    @Size(max = 255, message = "El email no puede exceder los 255 caracteres.")
    private String email;

    @NotNull(message = "El tipo de guardería es obligatorio.")
    private TypeDaycareEnum type;

    // Considera añadir @NotNull para latitude y longitude si siempre son obligatorios
    private Double latitude;
    private Double longitude;

    @NotNull(message = "La hora de apertura es obligatoria.")
    private LocalTime openingTime;

    @NotNull(message = "La hora de cierre es obligatoria.")
    private LocalTime closingTime;

    // --- NUEVOS CAMPOS A AGREGAR ---
    @NotNull(message = "La fecha de inicio del período de inscripción es obligatoria.")
    private LocalDate enrollmentPeriodStartDate;

    @NotNull(message = "La fecha de fin del período de inscripción es obligatoria.")
    private LocalDate enrollmentPeriodEndDate;

    // Lista opcional de definiciones de turno para la carga inicial
    private List<PostDaycareShiftDefinitionDto> shiftDefinitions;
}
