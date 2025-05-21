package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.dtos.put.PutAttendanceDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.models.Attendance;

import java.util.List;

public interface AttendanceService {
    //Buscar todas las asistenccias por niño [VER]
    List<GetAttendanceDto> getAllByChildId(Long childId);

    List<GetAttendanceDto> getAllByDaycareAndClassroom(Long daycareId, Long classroomId);

    List<GetAttendanceDto> createListofAttendanceforToday(Long daycareId, Long classroomId, ShiftEnum shift);
    List<GetAttendanceDto> updateAttendanceForToday(List<PutAttendanceDto> putAttendanceDtos);

}
