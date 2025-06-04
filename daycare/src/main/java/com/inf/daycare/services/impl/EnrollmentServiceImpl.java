package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetEnrollmentDto;
import com.inf.daycare.dtos.get.GetSingleEnrollmentDto;
import com.inf.daycare.dtos.post.PostEnrollmentDto;
import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.exceptions.EnrollmentConflictException;
import com.inf.daycare.exceptions.NoAvailableSlotsException;
import com.inf.daycare.exceptions.UnauthorizedChildException;
import com.inf.daycare.mapper.EnrollmentMapper;
import com.inf.daycare.models.*;
import com.inf.daycare.repositories.EnrollmentRepository;
import com.inf.daycare.services.ChildService;
import com.inf.daycare.services.ClassroomService;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ChildService childService; // Inyectado para obtener y validar niños.
    private final DaycareService daycareService;
    private final ClassroomService classroomService;
    private final EnrollmentMapper enrollmentMapper;

    /**
     * Recupera una inscripción por su ID.
     * @param enrollmentId El ID de la inscripción.
     * @return DTO de la inscripción.
     * @throws EntityNotFoundException Si la inscripción no se encuentra.
     */
    @Override
    public GetSingleEnrollmentDto getById(Long enrollmentId) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    /**
     * Recupera todas las inscripciones asociadas a una guardería.
     * @param daycareId El ID de la guardería.
     * @return Lista de DTOs de inscripciones.
     */
    @Override
    public List<GetEnrollmentDto> getAllByDaycareId(Long daycareId) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByDaycareId(daycareId)
                .orElse(Collections.emptyList()); // Si no hay inscripciones, devuelve una lista vacía.
        return enrollmentMapper.listEnrollmentToGetEnrollmentDtoList(enrollments);
    }

    @Override
    @Transactional // Garantiza la atomicidad de la operación de creación.
    public GetSingleEnrollmentDto create(PostEnrollmentDto postEnrollmentDto, Long tutorId) {
        // 1. Obtener Guardería y Niño
        Daycare daycare = daycareService.getDaycareOrThrow(postEnrollmentDto.getDaycareId());
        Child child = childService.getChildOrThrow(postEnrollmentDto.getChildId());

        // 2. Validar que el niño pertenece al tutor autenticado.
        if (!childService.isChildAssociatedWithTutor(postEnrollmentDto.getChildId(), tutorId)) {
            throw new UnauthorizedChildException("El niño con ID " + postEnrollmentDto.getChildId() + " no está asociado al tutor autenticado (ID: " + tutorId + ").");
        }

        // 3. Validar que la fecha de la inscripción esté dentro del período global de inscripción de la guardería.
        if (daycare.getEnrollmentPeriodStartDate() == null || daycare.getEnrollmentPeriodEndDate() == null) {
            throw new IllegalArgumentException("La guardería no tiene definido un período global de inscripción.");
        }
        if (postEnrollmentDto.getStartDate().isBefore(daycare.getEnrollmentPeriodStartDate()) ||
                postEnrollmentDto.getEndDate().isAfter(daycare.getEnrollmentPeriodEndDate())) {
            throw new IllegalArgumentException("Las fechas de la inscripción (" + postEnrollmentDto.getStartDate() + " al " + postEnrollmentDto.getEndDate() + ") están fuera del período de inscripción permitido para esta guardería (" + daycare.getEnrollmentPeriodStartDate() + " al " + daycare.getEnrollmentPeriodEndDate() + ").");
        }
        // Validar que la inscripción no se intente hacer después de que el periodo de inscripción global haya terminado (si esa es la regla)
        if (LocalDate.now().isAfter(daycare.getEnrollmentPeriodEndDate())) {
            throw new IllegalArgumentException("No se pueden crear inscripciones para esta guardería. El período de inscripción ha finalizado el " + daycare.getEnrollmentPeriodEndDate() + ".");
        }
        // También puedes validar que la startDate de la inscripción no sea anterior a la fecha actual si es una inscripción "futura"
        if (postEnrollmentDto.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio de la inscripción no puede ser anterior a la fecha actual.");
        }


        // 4. Validar el turno solicitado (MAÑANA, TARDE, AMBOS) contra los horarios definidos de la guardería.
        // Aquí obtendrás la definición específica de hora para el turno solicitado
        DaycareShiftDefinition requestedShiftDefinition = daycareService.getDaycareShiftDefinitionOrThrow(daycare, postEnrollmentDto.getShift());

        // Puedes añadir validaciones adicionales aquí si lo necesitas.
        // Por ejemplo, que el postEnrollmentDto.getShift() sea MAÑANA y la guardería empiece a las 14:00, esto ya te daría error si la definición de MAÑANA para esa guardería tiene un horario no compatible.


        // --- FIN NUEVAS VALIDACIONES ---

        // 5. Validar que no haya duplicidad o conflicto de inscripción.
        // La query de conflicto ya usa el shift del DTO, así que es compatible.
        List<Enrollment> conflictingEnrollments = enrollmentRepository.findConflictingEnrollments(
                child,
                daycare,
                postEnrollmentDto.getShift(),
                postEnrollmentDto.getStartDate(),
                postEnrollmentDto.getEndDate()
        );

        if (!conflictingEnrollments.isEmpty()) {
            throw new EnrollmentConflictException("El niño ya tiene una inscripción activa o pendiente que se superpone con las fechas y el turno solicitado en esta guardería.");
        }

        // 6. Calcular la edad del niño.
        Integer childAge = childService.calculateChildAge(child);

        // 7. Encontrar salas elegibles.
        // Importante: Esta búsqueda ya asume que Classroom.shift es compatible con la definición de ShiftEnum.
        // La lógica de DaycareShiftDefinition es para validar la coherencia del turno solicitado,
        // y para poder mostrar los horarios si el turno es 'AMBOS', por ejemplo.
        List<Classroom> eligibleClassrooms = classroomService.getClassroomByAgeAndShiftAndDaycare(
                daycare.getId(),
                childAge,
                postEnrollmentDto.getShift()
        );

        // 8. Seleccionar la primera sala con cupo.
        Classroom classroomWithAvailablePlaces = eligibleClassrooms.stream()
                .filter(classroom -> classroomService.getAvailableSlots(classroom) >= 1)
                .findFirst()
                .orElse(null);

        // 9. Crear la inscripción o lanzar excepción si no hay lugar.
        if (classroomWithAvailablePlaces != null) {
            Enrollment enrollment = enrollmentMapper.postEnrollmentDtoToEnrollment(postEnrollmentDto);

            enrollment.setDaycare(daycare);
            enrollment.setChild(child);
            enrollment.setClassroom(classroomWithAvailablePlaces);
            enrollment.setShift(postEnrollmentDto.getShift()); // Mantiene el shift solicitado por el padre

            enrollmentRepository.save(enrollment);

            return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
        } else {
            throw new NoAvailableSlotsException("No hay lugares disponibles en la guardería que cumplan los criterios de edad, turno y fechas seleccionados.");
        }
    }

    /**
     * Actualiza el estado de una inscripción (ej. de PENDIENTE a ACTIVO, INACTIVO).
     * @param enrollmentId El ID de la inscripción a actualizar.
     * @param status El nuevo estado de la inscripción.
     * @return DTO de la inscripción actualizada.
     * @throws EntityNotFoundException Si la inscripción no se encuentra.
     */
    @Override
    @Transactional
    public GetSingleEnrollmentDto updateStatus(Long enrollmentId, StatusEnum status) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        enrollment.setStatus(status);
        enrollmentRepository.save(enrollment);
        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    /**
     * Actualiza el estado de pago de una inscripción.
     * @param enrollmentId El ID de la inscripción a actualizar.
     * @param paymentStatus El nuevo estado de pago (PAGADO, IMPAGO, PENDIENTE).
     * @return DTO de la inscripción actualizada.
     * @throws EntityNotFoundException Si la inscripción no se encuentra.
     */
    @Override
    @Transactional
    public GetSingleEnrollmentDto updatePaymentStatus(Long enrollmentId, PayStatusEnum paymentStatus) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        enrollment.setPaymentStatus(paymentStatus);
        enrollmentRepository.save(enrollment);
        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    //-------------------------------------Métodos auxiliares-------------------------------------//

    /**
     * Busca una inscripción por su ID. Lanza una excepción si no se encuentra.
     * @param enrollmentId El ID de la inscripción.
     * @return La entidad Enrollment si se encuentra.
     * @throws EntityNotFoundException Si la inscripción no existe.
     */
    public Enrollment getEnrollmentOrThrow(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la inscripción con ID: " + enrollmentId));
    }
}
