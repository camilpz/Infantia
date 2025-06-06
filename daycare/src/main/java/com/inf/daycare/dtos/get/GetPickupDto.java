package com.inf.daycare.dtos.get;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetPickupDto {
    private Long id;
    private Long childId;
    private String childFirstName; //Para mostrar en la UI
    private String childLastName;  //Para mostrar en la UI
    private Long authorizedPersonId;
    private String authorizedPersonFirstName; //Para mostrar en la UI
    private String authorizedPersonLastName;  //Para mostrar en la UI
    private String authorizedPersonRelationship; //Relación con el niño
    private Long attendanceId;
    private LocalDateTime pickupTime;
    private String notes;
}
