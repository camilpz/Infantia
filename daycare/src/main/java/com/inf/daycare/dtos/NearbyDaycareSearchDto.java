package com.inf.daycare.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NearbyDaycareSearchDto {
    @NotNull(message = "La latitud es requerida")
    private Double latitude;

    @NotNull(message = "La longitud es requerida")
    private Double longitude;

    @Min(value = 0, message = "El radio no puede ser negativo")
    private Double radiusKm; // Radio en kilómetros
}
