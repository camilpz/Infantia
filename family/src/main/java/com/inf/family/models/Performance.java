package com.inf.family.models;

import com.inf.family.enums.LevelEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "performance")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Un niño puede tener muchos registros de desempeño

    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne
    @JoinColumn(name = "performance_type_id", nullable = false)
    private PerformanceType performanceType;

    //Registra el
    private Long teacherId;

    private LocalDateTime date;
    private LevelEnum level;
    private String commentary;
}
