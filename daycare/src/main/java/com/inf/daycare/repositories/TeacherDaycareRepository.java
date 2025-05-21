package com.inf.daycare.repositories;

import com.inf.daycare.models.TeacherDaycare;
import com.inf.daycare.models.TeacherDaycareId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherDaycareRepository extends JpaRepository<TeacherDaycare, TeacherDaycareId> {
    boolean existsByTeacherIdAndDaycareId(Long teacherId, Long daycareId);
}
