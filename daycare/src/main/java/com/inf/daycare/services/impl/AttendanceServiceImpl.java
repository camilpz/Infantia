package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.dtos.put.PutAttendanceDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.mapper.AttendanceMapper;
import com.inf.daycare.models.Attendance;
import com.inf.daycare.models.Enrollment;
import com.inf.daycare.repositories.AttendanceRepository;
import com.inf.daycare.repositories.EnrollmentRepository;
import com.inf.daycare.services.AttendanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public List<GetAttendanceDto> getAllByChildId(Long childId) {
        return List.of();
    }

    @Override
    public List<GetAttendanceDto> getAllByDaycareAndClassroom(Long daycareId, Long classroomId) {
        List<Attendance> attendances = attendanceRepository.findAllByDaycare_IdAndClassroom_Id(daycareId, classroomId)
                .orElse(Collections.emptyList());

        return attendanceMapper.listAttendanceToGetAttendanceDtoList(attendances);
    }

    @Override
    public List<GetAttendanceDto> createListofAttendanceforToday(Long daycareId, Long classroomId, ShiftEnum shift) {
        //Traer todas las inscripciones ACTIVAS de la guardería
        List<Enrollment> enrollments = enrollmentRepository.findAllByDaycareIdAndClassroomIdAndShiftAndStatus(
                        daycareId, classroomId, shift, StatusEnum.ACTIVO)
                .orElseThrow(() -> new EntityNotFoundException(""));

        //Filtrar las inscripciones por fecha (para que estén dentro de la duracion de la inscripcion)
       enrollments = enrollments.stream()
           .filter(e -> e.getStartDate() != null
               && e.getStartDate().getYear() == LocalDate.now().getYear()
               && !LocalDate.now().isBefore(e.getStartDate())
               && (e.getEndDate() == null || !LocalDate.now().isAfter(e.getEndDate())))
           .toList();

       //aqui hago la consulta a mi micro de child y traigo sus datos
        //Hago una lista y agrego postAttendanceDto por cada dato de cada niño
        //Retorno la lista

        return new ArrayList<GetAttendanceDto>();
    }

    @Override
    public List<GetAttendanceDto> updateAttendanceForToday(List<PutAttendanceDto> putAttendanceDtos) {
        return List.of();
    }
}
