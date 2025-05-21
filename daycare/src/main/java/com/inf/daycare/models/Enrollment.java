package com.inf.daycare.models;

import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "daycare_id", nullable = false)
    private Daycare daycare;

    @ManyToOne // Una inscripción pertenece a una sala específica
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    private Long childId; // This ID comes from another microservice

    private LocalDate createdAt;
    private String enrollmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ShiftEnum shift;

    private StatusEnum status; // "active", "inactive", "pending"
    private PayStatusEnum paymentStatus; // "paid", "unpaid", "pending"

    @PrePersist
    public void prePersist() {
        this.status = StatusEnum.PENDIENTE;
        this.paymentStatus = PayStatusEnum.PENDIENTE;
        this.createdAt = LocalDate.now();
    }
}
