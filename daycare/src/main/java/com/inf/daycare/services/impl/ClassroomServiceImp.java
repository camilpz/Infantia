package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.exceptions.NoAvailableSlotsException;
import com.inf.daycare.exceptions.RelationAlreadyExistsException;
import com.inf.daycare.mapper.ClassroomMapper;
import com.inf.daycare.models.Classroom;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.TeacherClassroom;
import com.inf.daycare.repositories.ClassroomRepository;
import com.inf.daycare.repositories.EnrollmentRepository;
import com.inf.daycare.repositories.TeacherClassroomRepository;
import com.inf.daycare.services.ClassroomService;
import com.inf.daycare.services.DaycareService;
import com.inf.daycare.services.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImp implements ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassroomMapper classroomMapper;
    private final TeacherService teacherService;
    private final TeacherClassroomRepository teacherClassroomRepository;
    private final DaycareService daycareService;

    @Override
    public GetClassroomDto getById(Long classroomId) {
        Classroom classroom = getClassroomOrThrow(classroomId);

        return classroomMapper.classroomToGetClassroomDto(classroom);
    }

    @Override
    public List<GetClassroomDto> getAllByDaycareId(Long daycareId) {
        List<Classroom> classrooms = classroomRepository.findAllByDaycareId(daycareId)
                .orElse(Collections.emptyList());

        return classroomMapper.classroomToGetClassroomDto(classrooms);
    }

    @Override
    @Transactional
    public GetClassroomDto create(PostClassroomDto postClassroomDto) {
        //Obtengo la guardería
        Daycare daycare = daycareService.getDaycareOrThrow(postClassroomDto.getDaycareId());

        //Creo la sala
        Classroom classroom = classroomMapper.postClassroomDtoToClassroom(postClassroomDto);
        classroom.setDaycare(daycare);

        classroomRepository.save(classroom);

        return classroomMapper.classroomToGetClassroomDto(classroom);
    }

    @Override
    public GetClassroomDto update(Long classroomId, PostClassroomDto postClassroomDto) {
        Classroom classroom = getClassroomOrThrow(classroomId);

        classroomMapper.updateClassroomFromPutClassroomDto(postClassroomDto, classroom);

        classroomRepository.save(classroom);

        return classroomMapper.classroomToGetClassroomDto(classroom);
    }

    //Obtengo los lugares disponibles en cada sala
    @Override
    public int getAvailableSlots(Classroom classroom) {
        int capacity = classroom.getCapacity();

        //TODO[Ver de cambiar por activo]
        long activeEnrollments = enrollmentRepository.countByClassroomAndStatus(classroom, StatusEnum.ACTIVO);
        return capacity - (int) activeEnrollments;
    }

    //Obtener las salas disponibles segun edad, turno (MAÑANA Y TARDE) y guarderia
    @Override
    public List<Classroom> getClassroomByAgeAndShiftAndDaycare(Long daycareId, int age, ShiftEnum shift) {
        return classroomRepository.findAllByDaycare_IdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqualAndShift(daycareId, age, age, shift)
                .orElseThrow(() -> new NoAvailableSlotsException("No hay salas disponibles"));
    }

    //Obtener las salas disponibles segun edad y guarderia. TOMA AMBOS TURNOS
    @Override
    public List<Classroom> getClassroomByAgeAndDaycare(Long daycareId, int age) {
//        return classroomRepository.findAllByDaycare_IdAndAgeMinLessThanEqualAndAgeMaxGreaterThanEqual(daycareId, age)
//                .orElseThrow(() -> new NoAvailableSlotsException("No hay salas disponibles"));
        return null;
    }

    @Override
    public void disableClassroom(Long classroomId) {
        Classroom classroom = getClassroomOrThrow(classroomId);
        classroom.setEnabled(false);

        classroomRepository.save(classroom);
    }

    @Override
    public void enableClassroom(Long classroomId) {
        Classroom classroom = getClassroomOrThrow(classroomId);
        classroom.setEnabled(true);

        classroomRepository.save(classroom);
    }

    public boolean addTeacherToClassroom(Long classroomId, Long teacherId, boolean isPrincipal) {
        if(!teacherClassroomRepository.existsByTeacherIdAndClassroomId(teacherId, classroomId)) {
            TeacherClassroom teacherClassroom = TeacherClassroom.builder()
                    .teacher(teacherService.getTeacherOrThrow(teacherId))
                    .classroom(getClassroomOrThrow(classroomId))
                    .isPrincipal(isPrincipal)
                    .build();

            teacherClassroomRepository.save(teacherClassroom);
        }
        else throw new RelationAlreadyExistsException("Ya existe una relación entre el profesor y la sala");

        return true;
    }

    //--------------------------------------Métods auxiliares--------------------------------

    Classroom getClassroomOrThrow(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
    }
}
