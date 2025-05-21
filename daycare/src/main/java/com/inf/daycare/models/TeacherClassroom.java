package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Table(name = "teacher_classroom")
@Entity
@Builder
@AllArgsConstructor
public class TeacherClassroom {

    @EmbeddedId
    private TeacherClassroomId id;

    private Boolean isPrincipal;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @MapsId("classroomId")
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    // Constructor vacío obligatorio
    public TeacherClassroom() {}

    public TeacherClassroom(Teacher teacher, Classroom classroom, Boolean isPrincipal) {
        this.teacher = teacher;
        this.classroom = classroom;
        this.id = new TeacherClassroomId(teacher.getId(), classroom.getId());
        this.isPrincipal = isPrincipal;
    }

    // Getters y Setters
    public TeacherClassroomId getId() {
        return id;
    }

    public void setId(TeacherClassroomId id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Boolean getIsPrincipal() {
        return isPrincipal;
    }

    public void setIsPrincipal(Boolean isPrincipal) {
        this.isPrincipal = isPrincipal;
    }
}
