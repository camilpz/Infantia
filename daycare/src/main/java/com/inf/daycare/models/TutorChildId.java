package com.inf.daycare.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Data
public class TutorChildId implements Serializable {
//    private Long tutorId;
//    private Long childId;
//
//    // equals() y hashCode()
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof TutorChildId)) return false;
//        TutorChildId that = (TutorChildId) o;
//        return Objects.equals(tutorId, that.tutorId) &&
//                Objects.equals(childId, that.childId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(tutorId, childId);
//    }
private Long tutorId;
    private Long childId;

    // Constructor vacío obligatorio
    public TutorChildId() {}

    public TutorChildId(Long tutorId, Long childId) {
        this.tutorId = tutorId;
        this.childId = childId;
    }

    // Getters y Setters
    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    // equals() y hashCode() son OBLIGATORIOS en clases @Embeddable
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TutorChildId)) return false;
        TutorChildId that = (TutorChildId) o;
        return Objects.equals(tutorId, that.tutorId) &&
                Objects.equals(childId, that.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tutorId, childId);
    }
}
