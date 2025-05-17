package com.inf.daycare.models;

import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.StatusEnum;
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

    @ManyToOne
    @JoinColumn(name = "daycare_id")
    private Daycare daycare;

    private String enrollmentDate;
    private String startDate;
    private String endDate;

    private StatusEnum status; // "active", "inactive", "pending"
    private PayStatusEnum paymentStatus; // "paid", "unpaid", "pending"

    @PrePersist
    public void prePersist() {
        this.status = StatusEnum.PENDIENTE;
        this.paymentStatus = PayStatusEnum.PENDIENTE;
    }
}
