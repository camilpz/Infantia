package com.inf.daycare.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Table(name = "tutor_child")
@Entity
@Data
@Builder
@AllArgsConstructor
public class TutorChild {

    @EmbeddedId
    private TutorChildId id;

    @ManyToOne
    @MapsId("tutorId")
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    @MapsId("childId")
    @JoinColumn(name = "child_id")
    private Child child;

    // Constructor vacío obligatorio
    public TutorChild() {}

    public TutorChild(Tutor tutor, Child child) {
        this.tutor = tutor;
        this.child = child;
        this.id = new TutorChildId(tutor.getId(), child.getId());
    }

    // Getters y Setters
    public TutorChildId getId() {
        return id;
    }

    public void setId(TutorChildId id) {
        this.id = id;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }
}
