package com.inf.daycare.repositories;

import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.models.Classroom;
import com.inf.daycare.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    //Busco una inscripcion en específico
    Optional<Enrollment> findByDaycareId(Long daycareId);

    //Busco todas las inscripciones de una guarderia
    Optional<List<Enrollment>> findAllByDaycareId(Long daycareId);

    long countByClassroomAndStatus(Classroom classroom, StatusEnum status);

    //TODO: Verificar si es necesario este método
    //Optional<Enrollment> findByChildIdAndClassroomIdAndDaycareIdAndStatus(Long childId, Long classroomId, StatusEnum status);

    // Método recomendado para obtener una única inscripción activa por niño, guardería y turno
//    Optional<Enrollment> findByChildIdAndDaycareIdAndShiftAndStatus(
//            Long childId,
//            Long daycareId,
//            ShiftEnum shift, //El turno específico que el usuario selecciona (MAÑANA o TARDE)
//            StatusEnum status //El estado de la inscripción (ACTIVO)
//    );

    //Obtener todas las inscripciones activas por guardería, sala y turno
    Optional<List<Enrollment>> findAllByDaycareIdAndClassroomIdAndShiftAndStatus(
            Long daycareId,
            Long classroomId,
            ShiftEnum shift,
            StatusEnum status
    );
}
