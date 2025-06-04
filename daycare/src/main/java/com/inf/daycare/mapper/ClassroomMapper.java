package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.dtos.put.PutClassroomDto;
import com.inf.daycare.models.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface ClassroomMapper {

    //@Mapping(target = "daycare", ignore = true)
    //@Mapping(target = "enrollments", ignore = true)
    //@Mapping(target = "activities", source = "activities") // Ignorar actividades en Classroom
    GetClassroomDto classroomToGetClassroomDto(Classroom classroom);

    // ... (el resto de tu código del ClassroomMapper, que ya se veía bien con los 'ignore = true' para Post y Put DTOs)

    // List<Classroom> to List<GetClassroomDto>
    List<GetClassroomDto> classroomToGetClassroomDto(List<Classroom> classrooms);

    // PostClassroomDto to Classroom
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "daycare", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "activities", ignore = true)
    Classroom postClassroomDtoToClassroom(PostClassroomDto postClassroomDto);

    // Update Classroom from PutClassroomDto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "daycare", ignore = true)
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "activities", ignore = true)
    void updateClassroomFromPutClassroomDto(PutClassroomDto putClassroomDto, @MappingTarget Classroom classroom);
}
