package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "enrollment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relaciones
    private Long childId;
    private Long daycareId;

    private String enrollmentDate;
    private String startDate;
    private String endDate;

    private String status; // "active", "inactive", "pending"
    private String paymentStatus; // "paid", "unpaid", "pending"

    
}
