package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Table(name = "teacher_daycare")
@Entity
@Builder
@AllArgsConstructor
public class TeacherDaycare {

    @EmbeddedId
    private TeacherDaycareId id;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @MapsId("daycareId")
    @JoinColumn(name = "daycare_id")
    private Daycare daycare;

    // Constructor vacío obligatorio
    public TeacherDaycare() {}

    public TeacherDaycare(Teacher teacher, Daycare daycare) {
        this.teacher = teacher;
        this.daycare = daycare;
        this.id = new TeacherDaycareId(teacher.getId(), daycare.getId());
    }

    // Getters y Setters
    public TeacherDaycareId getId() {
        return id;
    }

    public void setId(TeacherDaycareId id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Daycare getDaycare() {
        return daycare;
    }

    public void setDaycare(Daycare child) {
        this.daycare = child;
    }
}
