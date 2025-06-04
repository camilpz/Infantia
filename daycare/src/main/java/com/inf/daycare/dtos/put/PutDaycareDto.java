package com.inf.daycare.dtos.put;

import com.inf.daycare.enums.TypeDaycareEnum;
import lombok.Data;

@Data
public class PutDaycareDto {
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
    private String email;
    private TypeDaycareEnum type;
    private Double latitude;
    private Double longitude;
}
