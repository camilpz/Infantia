package com.inf.daycare.repositories;

import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<List<Classroom>> findAllByDaycareId(Long daycareId);
    Optional<List<Classroom>> findAllByDaycare_IdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqualAndShift(Long daycareId,
                                                                                                         int ageMin,
                                                                                                         int ageMax,
                                                                                                         ShiftEnum shift);
    Optional<List<Classroom>> findAllByDaycare_IdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(Long daycareId, int ageMin, int ageMax);
}
