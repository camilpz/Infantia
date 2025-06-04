package com.inf.daycare.repositories;

import com.inf.daycare.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllByTutor_Id(Long tutorId);

    // Buscar comentarios por tutor y guardería, y que estén dentro de un rango de fechas
    Long countByTutor_IdAndDaycare_IdAndCreatedAtBetween(Long tutorId, Long daycareId, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);
    //Optional<List<Comment>> findAllByDaycare_idOrderByCreatedAtDesc(Long daycareId);
    Optional<List<Comment>> findByDaycareIdOrderByCreatedAtDesc(Long daycareId);
}
