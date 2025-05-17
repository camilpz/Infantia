package com.inf.daycare.models;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Table(name = "classroom_daycare")
@Entity
@Data
public class ClassroomDaycare {
    @EmbeddedId
    private ClassroomDaycareId id;
}
