package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetEnrollmentDto;
import com.inf.daycare.dtos.get.GetSingleEnrollmentDto;
import com.inf.daycare.dtos.post.PostEnrollmentDto;
import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.exceptions.NoAvailableSlotsException;
import com.inf.daycare.mapper.EnrollmentMapper;
import com.inf.daycare.models.Classroom;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.Enrollment;
import com.inf.daycare.repositories.EnrollmentRepository;
import com.inf.daycare.services.ClassroomService;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final DaycareService daycareService;
    private final ClassroomService classroomService;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public GetSingleEnrollmentDto getById(Long enrollmentId) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);

        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    @Override
    public List<GetEnrollmentDto> getAllByDaycareId(Long daycareId) {
        List<Enrollment> enrollments = enrollmentRepository.findAllByDaycareId(daycareId)
                .orElse(Collections.emptyList());

        return enrollmentMapper.listEnrollmentToGetEnrollmentDtoList(enrollments);
    }

    @Override
    @Transactional
    public GetSingleEnrollmentDto create(PostEnrollmentDto postEnrollmentDto) {
        //Verificar y obtener la guardería
        Daycare daycare = daycareService.getDaycareOrThrow(postEnrollmentDto.getDaycareId());

        List<Classroom> classrooms;

        //Filtrar por turno y rango de edad, si no hay tira una excepción el método
        if(postEnrollmentDto.getShift() == ShiftEnum.AMBOS){
            classrooms = classroomService.getClassroomByAgeAndDaycare(daycare.getId(), postEnrollmentDto.getChildAge());
        }
        else {
            classrooms = classroomService.getClassroomByAgeAndShiftAndDaycare(daycare.getId(), postEnrollmentDto.getChildAge(), postEnrollmentDto.getShift());
        }

        //Ver si está disponible por capacidad

        Classroom classroomWithAvailablePlaces = classrooms.stream()
            .filter(classroom -> classroomService.getAvailableSlots(classroom) >= 1)
            .findFirst()
            .orElse(null);

        //Si hay lugar, se crea la inscripción
        if (classroomWithAvailablePlaces != null) {
            Enrollment enrollment = enrollmentMapper.postEnrollmentDtoToEnrollment(postEnrollmentDto);
            enrollment.setDaycare(daycare);
            enrollment.setClassroom(classroomWithAvailablePlaces);

            enrollmentRepository.save(enrollment);

            return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
        } else {
            throw new NoAvailableSlotsException("No hay lugares disponibles en la guardería");
        }
    }

    @Override
    public GetSingleEnrollmentDto updateStatus(Long enrollmentId, StatusEnum status) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        enrollment.setStatus(status);

        enrollmentRepository.save(enrollment);

        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    @Override
    public GetSingleEnrollmentDto updatePaymentStatus(Long enrollmentId, PayStatusEnum paymentStatus) {
        Enrollment enrollment = getEnrollmentOrThrow(enrollmentId);
        enrollment.setPaymentStatus(paymentStatus);

        enrollmentRepository.save(enrollment);

        return enrollmentMapper.enrollmentToGetSimpleEnrollmentDto(enrollment);
    }

    //-------------------------------------Métodos auxiliares-------------------------------------//

    public Enrollment getEnrollmentOrThrow(Long enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la inscripción"));
    }
}
