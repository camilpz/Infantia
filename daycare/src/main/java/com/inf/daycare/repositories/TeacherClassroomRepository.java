package com.inf.daycare.repositories;

import com.inf.daycare.models.TeacherClassroom;
import com.inf.daycare.models.TeacherClassroomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherClassroomRepository extends JpaRepository<TeacherClassroom, TeacherClassroomId> {
    boolean existsByTeacherIdAndClassroomId(Long teacherId, Long classroomId);
}
