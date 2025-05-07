package com.inf.daycare.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Table(name = "teacher_daycare")
@Entity
@Data
@Builder
@AllArgsConstructor
public class TeacherDaycare {

    @EmbeddedId
    private TeacherDaycareId id;
}
