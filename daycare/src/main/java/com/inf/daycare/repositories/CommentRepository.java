package com.inf.daycare.repositories;

import com.inf.daycare.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findAllByParentId(Long parentId);
    Optional<List<Comment>> findAllByDaycare_idOrderByCreatedAtDesc(Long daycareId);
}
