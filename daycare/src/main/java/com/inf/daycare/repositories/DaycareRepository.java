package com.inf.daycare.repositories;

import com.inf.daycare.models.Daycare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DaycareRepository extends JpaRepository<Daycare, Long> {
    Optional<List<Daycare>> findAllByDirectorId(Long directorId);
}
