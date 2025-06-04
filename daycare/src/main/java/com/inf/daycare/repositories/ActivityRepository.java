package com.inf.daycare.repositories;

import com.inf.daycare.enums.DayOfWeekEnum;
import com.inf.daycare.models.Activity;
import com.inf.daycare.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByClassroom(Classroom classroom);
    List<Activity> findAllByClassroomAndDayOfWeek(Classroom classroom, DayOfWeekEnum dayOfWeek);
}
