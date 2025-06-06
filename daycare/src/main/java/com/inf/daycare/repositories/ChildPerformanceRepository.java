package com.inf.daycare.repositories;

import com.inf.daycare.enums.DevelopmentAreaEnum;
import com.inf.daycare.models.ChildPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChildPerformanceRepository extends JpaRepository<ChildPerformance, Long> {
    List<ChildPerformance> findByChild_IdOrderByEvaluationDateDesc(Long childId);

    List<ChildPerformance> findByChild_IdAndDevelopmentAreaOrderByEvaluationDateDesc(Long childId, DevelopmentAreaEnum developmentArea);

    List<ChildPerformance> findByTeacher_IdOrderByEvaluationDateDesc(Long teacherUserId);

    List<ChildPerformance> findByEvaluationDate(LocalDate date);
}
