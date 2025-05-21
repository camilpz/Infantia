package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.models.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {
    //Classroom to GetClassroomDto
    GetClassroomDto classroomToGetClassroomDto(Classroom classroom);

    //List<Classroom> to List<GetClassroomDto>
    List<GetClassroomDto> classroomToGetClassroomDto(List<Classroom> classrooms);

    //PostClassroomDto to Classroom
    Classroom postClassroomDtoToClassroom(PostClassroomDto postClassroomDto);

    //Update Classroom from PutClassroomDto
    void updateClassroomFromPutClassroomDto(PostClassroomDto postClassroomDto, @MappingTarget Classroom classroom);
}
