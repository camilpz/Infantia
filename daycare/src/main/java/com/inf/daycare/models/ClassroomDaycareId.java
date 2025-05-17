package com.inf.daycare.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Objects;

@Embeddable
@Data
public class ClassroomDaycareId {
    private Long classroomId;
    private Long daycareId;

    public ClassroomDaycareId() {}

    public ClassroomDaycareId(Long classroomId, Long daycareId) {
        this.classroomId = classroomId;
        this.daycareId = daycareId;
    }

    // equals() y hashCode() son OBLIGATORIOS en clases @Embeddable
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassroomDaycareId)) return false;
        ClassroomDaycareId that = (ClassroomDaycareId) o;
        return Objects.equals(classroomId, that.classroomId) &&
                Objects.equals(daycareId, that.daycareId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classroomId, daycareId);
    }
}
