package com.inf.daycare.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Objects;

@Embeddable
@Data
public class TeacherDaycareId {
    private Long teacherId;
    private Long daycareId;

    public TeacherDaycareId() {}

    public TeacherDaycareId(Long teacherId, Long daycareId) {
        this.teacherId = teacherId;
        this.daycareId = daycareId;
    }

    // equals() y hashCode() son OBLIGATORIOS en clases @Embeddable
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherDaycareId)) return false;
        TeacherDaycareId that = (TeacherDaycareId) o;
        return Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(daycareId, that.daycareId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, daycareId);
    }
}
