package com.inf.daycare.repositories;

import com.inf.daycare.models.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PickupRepository extends JpaRepository<Pickup, Long> {
    List<Pickup> findByChild_Id(Long childId);

    // Buscar registros de retiro para una fecha específica (del pickupTime)
    List<Pickup> findByPickupTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Pickup> findByAttendance_Id(Long attendanceId);
}
