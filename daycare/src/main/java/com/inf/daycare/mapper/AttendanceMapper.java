package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetAttendanceDto;
import com.inf.daycare.dtos.post.PostAttendanceDto;
import com.inf.daycare.dtos.put.PutAttendanceDto;
import com.inf.daycare.models.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    // Attendance to GetAttendanceDto
    @Mapping(target = "childId", source = "child.id") // Mapea el ID del Child de la entidad
    GetAttendanceDto attendanceToGetAttendanceDto(Attendance attendance);

    // PostAttendanceDto to Attendance
    // Ignorar ID (autogenerado), y las relaciones se setearán en el servicio
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "daycare", ignore = true)
    @Mapping(target = "classroom", ignore = true)
    @Mapping(target = "child", ignore = true)
    @Mapping(target = "checkInTime", ignore = true) // Se maneja con @PrePersist o en el servicio
    @Mapping(target = "checkOutTime", ignore = true) // Puede ser nulo o se setea en update
    Attendance postAttendanceDtoToAttendance(PostAttendanceDto postAttendanceDto);

    // List<Attendance> to List<GetAttendanceDto>
    List<GetAttendanceDto> listAttendanceToGetAttendanceDtoList(List<Attendance> attendances);

    // Update Attendance from PutAttendanceDto
    // Aquí puedes decidir qué campos se pueden actualizar.
    // Generalmente, ID, y las relaciones no se actualizan por aquí.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    @Mapping(target = "daycare", ignore = true)
    @Mapping(target = "classroom", ignore = true)
    @Mapping(target = "child", ignore = true)
    @Mapping(target = "attendanceDate", ignore = true) // La fecha no se debería cambiar en un PUT
    @Mapping(target = "checkInTime", ignore = true) // checkInTime no se debería cambiar en un PUT
    void updateAttendanceFromPutAttendanceDto(PutAttendanceDto putAttendanceDto, @MappingTarget Attendance attendance);
}
