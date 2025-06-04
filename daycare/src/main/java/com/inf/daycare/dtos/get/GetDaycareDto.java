package com.inf.daycare.dtos.get;

import com.inf.daycare.enums.TypeDaycareEnum;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalTime;

@Data
public class GetDaycareDto {
    private Long id;

    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
    private String email;
    private Boolean enabled;
    private TypeDaycareEnum type;
    private Double latitude;
    private Double longitude;
    private LocalTime openingTime;
    private LocalTime closingTime;
}
