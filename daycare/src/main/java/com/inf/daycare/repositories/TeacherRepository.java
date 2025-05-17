package com.inf.daycare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inf.daycare.models.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    // Custom query methods can be defined here if needed
    Optional<Teacher> findByUserId(Long userId);
}
