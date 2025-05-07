package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "daycare")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Daycare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
