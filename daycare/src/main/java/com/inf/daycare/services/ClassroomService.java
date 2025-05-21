package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.models.Classroom;

import java.util.List;

public interface ClassroomService {
    GetClassroomDto getById(Long classroomId);
    List<GetClassroomDto> getAllByDaycareId(Long daycareId);
    GetClassroomDto create(PostClassroomDto postClassroomDto);
    GetClassroomDto update(Long id, PostClassroomDto postClassroomDto);
    int getAvailableSlots(Classroom classroom);
    List<Classroom> getClassroomByAgeAndShiftAndDaycare(Long daycareId, int age, ShiftEnum shift);
    List<Classroom> getClassroomByAgeAndDaycare(Long daycareId, int age);
    void disableClassroom(Long classroomId);
    void enableClassroom(Long classroomId);
    boolean addTeacherToClassroom(Long classroomId, Long teacherId, boolean isPrincipal);
}
