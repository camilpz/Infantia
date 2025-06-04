package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetActivityDto;
import com.inf.daycare.dtos.post.PostActivityDto;
import com.inf.daycare.dtos.put.PutActivityDto;
import com.inf.daycare.exceptions.InvalidScheduleException;
import com.inf.daycare.exceptions.ScheduleConflictException;
import com.inf.daycare.mapper.ActivityMapper;
import com.inf.daycare.models.Activity;
import com.inf.daycare.models.Classroom;
import com.inf.daycare.repositories.ActivityRepository;
import com.inf.daycare.services.ActivityService;
import com.inf.daycare.services.ClassroomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final ClassroomService classroomService;

    // Método para obtener una actividad por ID
    @Override
    public GetActivityDto getActivityById(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
        return activityMapper.activityToGetActivityDto(activity);
    }

    // Método para obtener todas las actividades de una sala
    @Override
    public List<GetActivityDto> getAllActivitiesByClassroomId(Long classroomId) {
        Classroom classroom = classroomService.getClassroomOrThrow(classroomId); // Asegura que la sala exista

        List<Activity> activities = activityRepository.findAllByClassroom(classroom);
        return activityMapper.activityToGetActivityDto(activities); // Convierte a Set si es necesario
    }

    @Override
    @Transactional
    public GetActivityDto createActivity(PostActivityDto postActivityDto) {
        // 0. Validar que la hora de inicio sea menor que la hora de fin
        if (postActivityDto.getStartTime().isAfter(postActivityDto.getEndTime()) ||
                postActivityDto.getStartTime().equals(postActivityDto.getEndTime())) {
            throw new InvalidScheduleException("La hora de inicio debe ser menor que la hora de fin de la actividad.");
        }

        Classroom classroom = classroomService.getClassroomOrThrow(postActivityDto.getClassroomId());

        // 1. Verificar conflictos de horario
        if (hasScheduleConflict(classroom, postActivityDto)) {
            throw new ScheduleConflictException("El horario de la actividad se superpone con otra actividad existente en esta sala.");
        }

        Activity activity = activityMapper.postActivityDtoToActivity(postActivityDto);
        activity.setClassroom(classroom);

        activityRepository.save(activity);
        return activityMapper.activityToGetActivityDto(activity);
    }

    @Override
    @Transactional
    public GetActivityDto updateActivity(Long activityId, PutActivityDto updatedActivityDto) { // Podrías usar PutActivityDto
        // También debes añadir la validación de hora de inicio < hora de fin aquí
        if (updatedActivityDto.getStartTime().isAfter(updatedActivityDto.getEndTime()) ||
                updatedActivityDto.getStartTime().equals(updatedActivityDto.getEndTime())) {
            throw new InvalidScheduleException("La hora de inicio debe ser menor que la hora de fin de la actividad.");
        }

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));

        // Al actualizar, necesitas considerar que la actividad que estás actualizando
        // NO debe generar conflicto consigo misma si cambias sus horarios.
        // La validación de conflicto debe excluir la actividad que se está actualizando.
        if (hasScheduleConflictForUpdate(activity.getClassroom(), updatedActivityDto, activity.getId())) {
            throw new ScheduleConflictException("El horario de la actividad se superpone con otra actividad existente en esta sala.");
        }


        // Actualiza los campos que te interesen
        activity.setName(updatedActivityDto.getName());
        activity.setDescription(updatedActivityDto.getDescription());
        activity.setDayOfWeek(updatedActivityDto.getDayOfWeek());
        activity.setStartTime(updatedActivityDto.getStartTime());
        activity.setEndTime(updatedActivityDto.getEndTime());

        activityRepository.save(activity);
        return activityMapper.activityToGetActivityDto(activity);
    }

    // Método para eliminar una actividad
    @Override
    @Transactional
    public void deleteActivity(Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
        activityRepository.delete(activity);
    }

    //-----------------------------------------Métodos auxiliares-----------------------------------------

    @Override
    public Activity getActivityOrThrow(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));
    }

    // Método auxiliar para verificar conflictos (casi igual, solo aseguramos el tipo en la firma)
    private boolean hasScheduleConflict(Classroom classroom, PostActivityDto newActivityDto) {
        List<Activity> existingActivities = activityRepository.findAllByClassroomAndDayOfWeek(classroom, newActivityDto.getDayOfWeek());

        LocalTime newStartTime = newActivityDto.getStartTime();
        LocalTime newEndTime = newActivityDto.getEndTime();

        for (Activity existingActivity : existingActivities) {
            LocalTime existingStartTime = existingActivity.getStartTime();
            LocalTime existingEndTime = existingActivity.getEndTime();

            // Caso 1: La nueva actividad empieza antes de que termine una existente y termina después de que empiece.
            // Es decir, hay solapamiento.
            // [EXISTING_START --- NEW_START --- EXISTING_END --- NEW_END]
            // [NEW_START --- EXISTING_START --- NEW_END --- EXISTING_END]
            // [NEW_START --- EXISTING_START --- EXISTING_END --- NEW_END] (Nueva envuelve existente)
            // [EXISTING_START --- NEW_START --- NEW_END --- EXISTING_END] (Existente envuelve nueva)
            // [NEW_START === EXISTING_START --- NEW_END === EXISTING_END] (Horarios idénticos)

            // Simplificación lógica de superposición:
            // Dos intervalos [A, B] y [C, D] se superponen si (A < D AND C < B)
            if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
                return true; // Conflicto
            }
        }
        return false; // No hay conflicto
    }

    // Nuevo método auxiliar para la validación de conflicto en update, excluyendo la propia actividad
    private boolean hasScheduleConflictForUpdate(Classroom classroom, PutActivityDto updatedActivityDto, Long currentActivityId) {
        List<Activity> existingActivities = activityRepository.findAllByClassroomAndDayOfWeek(classroom, updatedActivityDto.getDayOfWeek());

        LocalTime newStartTime = updatedActivityDto.getStartTime();
        LocalTime newEndTime = updatedActivityDto.getEndTime();

        for (Activity existingActivity : existingActivities) {
            // Ignorar la actividad que estamos actualizando
            if (existingActivity.getId().equals(currentActivityId)) {
                continue;
            }

            LocalTime existingStartTime = existingActivity.getStartTime();
            LocalTime existingEndTime = existingActivity.getEndTime();

            if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
                return true; // Conflicto
            }
        }
        return false; // No hay conflicto
    }
}
