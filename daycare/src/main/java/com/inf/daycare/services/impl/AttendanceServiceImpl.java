package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.dtos.put.PutAttendanceDto;
import com.inf.daycare.enums.AttendanceEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.mapper.AttendanceMapper;
import com.inf.daycare.models.Attendance;
import com.inf.daycare.models.Classroom;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.Enrollment;
import com.inf.daycare.repositories.*;
import com.inf.daycare.services.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ChildRepository childRepository; // Necesitarás este repositorio
    private final DaycareRepository daycareRepository; // Necesitarás este repositorio
    private final ClassroomRepository classroomRepository; // Necesitarás este repositorio
    private final AttendanceMapper attendanceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GetAttendanceDto> getAllByChildId(Long childId) {
        List<Attendance> attendances = attendanceRepository.findAllByChild_Id(childId);
        return attendanceMapper.listAttendanceToGetAttendanceDtoList(attendances);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAttendanceDto> getAllByDaycareAndClassroom(Long daycareId, Long classroomId) {
        List<Attendance> attendances = attendanceRepository.findAllByDaycare_IdAndClassroom_Id(daycareId, classroomId)
                .orElse(Collections.emptyList()); // Considera si el orElseThrow es mejor aquí si no se encuentran
        return attendanceMapper.listAttendanceToGetAttendanceDtoList(attendances);
    }

    @Override
    @Transactional
    public List<GetAttendanceDto> createListofAttendanceforToday(Long daycareId, Long classroomId, ShiftEnum shift) {
        // 1. Traer todas las inscripciones ACTIVAS de la guardería para el aula y turno especificado
        List<Enrollment> enrollments = enrollmentRepository.findAllByDaycareIdAndClassroomIdAndShiftAndStatus(
                        daycareId, classroomId, shift, StatusEnum.ACTIVO)
                .orElse(Collections.emptyList()); // Usar emptyList en lugar de orElseThrow aquí si quieres crear para los que existen

        // 2. Filtrar las inscripciones por fecha (para que estén dentro de la duración de la inscripción)
        enrollments = enrollments.stream()
                .filter(e -> e.getStartDate() != null
                        && !LocalDate.now().isBefore(e.getStartDate()) // attendanceDate >= startDate
                        && (e.getEndDate() == null || !LocalDate.now().isAfter(e.getEndDate()))) // attendanceDate <= endDate o endDate es nulo
                .toList();

        if (enrollments.isEmpty()) {
            // Si no hay inscripciones activas válidas para hoy, podrías devolver una lista vacía
            // o lanzar una excepción si es un error esperable.
            return Collections.emptyList();
        }

        // Obtener las entidades Daycare y Classroom para establecer las relaciones directas
        Daycare daycare = daycareRepository.findById(daycareId)
                .orElseThrow(() -> new EntityNotFoundException("Guardería no encontrada con ID: " + daycareId));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Aula no encontrada con ID: " + classroomId));

        // 3. Verificar si ya existe un registro de asistencia para hoy para alguno de los niños/inscripciones
        // Esto previene duplicados si la función se llama varias veces el mismo día.
        List<Long> enrolledChildIds = enrollments.stream()
                .map(e -> e.getChild().getId())
                .collect(Collectors.toList());

        List<Attendance> existingAttendances = attendanceRepository
                .findByChild_IdInAndAttendanceDateAndClassroom_Id(enrolledChildIds, LocalDate.now(), classroomId);

        List<Long> existingAttendanceChildIds = existingAttendances.stream()
                .map(a -> a.getChild().getId())
                .collect(Collectors.toList());

        // 4. Crear registros de asistencia solo para los niños que no tienen uno hoy
        List<Attendance> newAttendances = enrollments.stream()
                .filter(e -> !existingAttendanceChildIds.contains(e.getChild().getId()))
                .map(enrollment -> {
                    Attendance attendance = Attendance.builder()
                            .enrollment(enrollment)
                            .daycare(daycare) // Establecer la relación directa
                            .classroom(classroom) // Establecer la relación directa
                            .child(enrollment.getChild()) // Usar el Child de la inscripción
                            .attendanceDate(LocalDate.now())
                            .status(AttendanceEnum.AUSENTE) // Por defecto a AUSENTE, se actualizará después
                            .checkInTime(null) // Puede ser nulo o LocalDateTime.now() si al crear ya se marcan presentes
                            .checkOutTime(null)
                            .notes(null) // O un valor por defecto
                            .build();
                    // El @PrePersist en la entidad se encargará de checkInTime si no lo seteamos aquí.
                    // Si checkInTime es mandatorio y debe estar al crear, es mejor setearlo aquí.
                    // attendance.setCheckInTime(LocalDateTime.now());
                    return attendance;
                })
                .collect(Collectors.toList());

        // 5. Guardar las nuevas asistencias
        List<Attendance> savedAttendances = attendanceRepository.saveAll(newAttendances);

        // 6. Combinar con las existentes y mapear a DTOs
        List<Attendance> allTodayAttendances = new ArrayList<>();
        allTodayAttendances.addAll(savedAttendances);
        allTodayAttendances.addAll(existingAttendances); // Incluir las asistencias que ya existían

        return attendanceMapper.listAttendanceToGetAttendanceDtoList(allTodayAttendances);
    }


    @Override
    @Transactional
    public List<GetAttendanceDto> updateAttendanceForToday(List<PutAttendanceDto> putAttendanceDtos) {
        if (putAttendanceDtos == null || putAttendanceDtos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Attendance> updatedAttendances = new ArrayList<>();

        for (PutAttendanceDto dto : putAttendanceDtos) {
            // Asume que el PutAttendanceDto debe tener un ID para identificar la asistencia
            // Si el ID se envía como parte del path, necesitarás un método `updateAttendance(Long attendanceId, PutAttendanceDto dto)`
            // Por simplicidad, asumiré que el ID viene en el PutAttendanceDto.
            if (dto.getId() == null) {
                // Considera lanzar una excepción o loggear un error si falta el ID
                continue;
            }

            Attendance attendance = attendanceRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + dto.getId()));

            // Mapear campos actualizables usando MapStruct
            attendanceMapper.updateAttendanceFromPutAttendanceDto(dto, attendance);

            // Lógica específica para checkInTime y checkOutTime
            if (dto.getStatus() == AttendanceEnum.PRESENTE && attendance.getCheckInTime() == null) {
                attendance.setCheckInTime(LocalDateTime.now()); // Marca la entrada si es PRESENTE y no tenía hora
            }
            if (dto.getCheckOutTime() != null) {
                attendance.setCheckOutTime(dto.getCheckOutTime());
            }

            updatedAttendances.add(attendance);
        }
        // Guardar todas las asistencias actualizadas en una sola transacción
        List<Attendance> savedUpdatedAttendances = attendanceRepository.saveAll(updatedAttendances);
        return attendanceMapper.listAttendanceToGetAttendanceDtoList(savedUpdatedAttendances);
    }

    // Puedes agregar un método para obtener una sola asistencia por ID
    @Transactional(readOnly = true)
    public GetAttendanceDto getAttendanceById(Long attendanceId) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("Asistencia no encontrada con ID: " + attendanceId));
        return attendanceMapper.attendanceToGetAttendanceDto(attendance);
    }
}
