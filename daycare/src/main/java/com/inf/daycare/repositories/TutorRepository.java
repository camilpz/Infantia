package com.inf.daycare.repositories;

import com.inf.daycare.models.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByIdAndEnabledTrue(Long id);
    Optional<Tutor> findByUser_Id(Long userId);
}
