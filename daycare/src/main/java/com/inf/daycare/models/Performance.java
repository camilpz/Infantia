package com.inf.daycare.models;

import com.inf.daycare.enums.LevelEnum;
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

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private String commentary;

    @PrePersist
    private void prePersist() { date = LocalDateTime.now(); }
}
