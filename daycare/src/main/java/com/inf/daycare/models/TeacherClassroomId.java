package com.inf.daycare.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class TeacherClassroomId implements Serializable {
    private Long teacherId;
    private Long classroomId;

    public TeacherClassroomId() {}

    public TeacherClassroomId(Long teacherId, Long classroomId) {
        this.teacherId = teacherId;
        this.classroomId = classroomId;
    }

    // equals() y hashCode() son OBLIGATORIOS en clases @Embeddable
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherClassroomId)) return false;
        TeacherClassroomId that = (TeacherClassroomId) o;
        return Objects.equals(teacherId, that.teacherId) &&
                Objects.equals(classroomId, that.classroomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, classroomId);
    }
}
