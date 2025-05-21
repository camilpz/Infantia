package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.models.Attendance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    //Attendance to GetAttendanceDto
    GetAttendanceDto attendanceToGetAttendanceDto(Attendance attendance);

    //List<attendance> to List<GetAttendanceDto>
    List<GetAttendanceDto> listAttendanceToGetAttendanceDtoList(List<Attendance> attendances);
}
