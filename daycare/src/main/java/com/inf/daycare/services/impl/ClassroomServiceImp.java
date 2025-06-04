package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetActivityDto;
import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.dtos.put.PutClassroomDto;
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
import java.util.stream.Collectors;

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
    public GetClassroomDto update(Long classroomId, PutClassroomDto putClassroomDto) {
        Classroom classroom = getClassroomOrThrow(classroomId);

        classroomMapper.updateClassroomFromPutClassroomDto(putClassroomDto, classroom);

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
    public void changeStatus(Long classroomId, boolean status) {
        Classroom classroom = getClassroomOrThrow(classroomId);
        classroom.setEnabled(status);

        classroomRepository.save(classroom);
    }

    public void addTeacherToClassroom(Long classroomId, Long teacherId, boolean isPrincipal) {
        if(!teacherClassroomRepository.existsByTeacherIdAndClassroomId(teacherId, classroomId)) {
            TeacherClassroom teacherClassroom = new TeacherClassroom(teacherService.getTeacherOrThrow(teacherId),
                    getClassroomOrThrow(classroomId), isPrincipal);

            teacherClassroomRepository.save(teacherClassroom);
        }
        else throw new RelationAlreadyExistsException("Ya existe una relación entre el profesor y la sala");
    }

    //--------------------------------------Métods auxiliares--------------------------------

    @Override
    public Classroom getClassroomOrThrow(Long classroomId) {
        return classroomRepository.findById(classroomId)
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
    }


    /*public GetClassroomDto mapClassroomToGetClassroomDtoManual(Classroom classroom) {
        if (classroom == null) {
            return null;
        }

        GetClassroomDto dto = new GetClassroomDto();
        dto.setId(classroom.getId());
        dto.setName(classroom.getName());
        dto.setDescription(classroom.getDescription());
        dto.setAgeMin(classroom.getAgeMin());
        dto.setAgeMax(classroom.getAgeMax());
        dto.setShift(classroom.getShift());
        dto.setCapacity(classroom.getCapacity());
        dto.setEnabled(classroom.getEnabled());

        // Mapear actividades: aquí puedes usar tu ActivityMapper si funciona, o hacerlo manual también
        if (classroom.getActivities() != null && !classroom.getActivities().isEmpty()) {
            // Opción 1: Usar ActivityMapper (si ese mapper sí se genera bien)
            // dto.setActivities(activityMapper.activityToGetActivityDto(classroom.getActivities()));

            // Opción 2: Mapear actividades manualmente si ActivityMapper también falla o no quieres inyectarlo
            dto.setActivities(classroom.getActivities().stream()
                    .map(this::mapActivityToGetActivityDtoManual) // Usa un método helper privado
                    .collect(Collectors.toList()));
        }

        // NO mapear 'daycare' ni 'enrollments' ya que GetClassroomDto no los tiene.
        // Ni siquiera se mencionan aquí, que es lo que queremos.

        return dto;
    }

    // Método helper para mapear Activity a GetActivityDto manualmente
    private GetActivityDto mapActivityToGetActivityDtoManual(Activity activity) {
        if (activity == null) {
            return null;
        }
        GetActivityDto activityDto = new GetActivityDto();
        activityDto.setId(activity.getId());
        activityDto.setName(activity.getName());
        activityDto.setDescription(activity.getDescription());
        activityDto.setDayOfWeek(activity.getDayOfWeek());
        activityDto.setStartTime(activity.getStartTime());
        activityDto.setEndTime(activity.getEndTime());
        return activityDto;
    }


    // Método para mapear una lista de Classrooms manualmente
    public List<GetClassroomDto> mapClassroomsToGetClassroomDtosManual(List<Classroom> classrooms) {
        if (classrooms == null) {
            return null;
        }
        return classrooms.stream()
                .map(this::mapClassroomToGetClassroomDtoManual)
                .collect(Collectors.toList());
    }*/
}
