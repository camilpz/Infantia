package com.inf.daycare.repositories;

import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.DaycareShiftDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DaycareShiftDefinitionRepository extends JpaRepository<DaycareShiftDefinition, Integer> {
    Optional<DaycareShiftDefinition> findByDaycareAndShiftType(Daycare daycare, ShiftEnum shiftType);
}
