package com.inf.family.models;

import com.inf.family.enums.TurnEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "attendance")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    //Este id viene de otro micro
    private Long teacherId;

    private boolean presence;
    private LocalDateTime date;
    private TurnEnum turn;
    private String observation;
}
