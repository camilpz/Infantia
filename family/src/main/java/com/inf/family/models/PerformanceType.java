package com.inf.family.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "performance_type")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerformanceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
