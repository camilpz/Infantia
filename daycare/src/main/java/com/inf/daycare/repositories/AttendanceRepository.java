package com.inf.daycare.repositories;

import com.inf.daycare.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    //Trae todas las asistencias de una guardería y una determinada sala
    Optional<List<Attendance>> findAllByDaycare_IdAndClassroom_Id(Long daycareId, Long classroomId);

    //Trae todas las asistencias de un niño
    List<Attendance> findAllByChild_Id(Long childId);

    List<Attendance> findByChild_IdInAndAttendanceDateAndClassroom_Id(List<Long> childIds, LocalDate attendanceDate, Long classroomId);
}
