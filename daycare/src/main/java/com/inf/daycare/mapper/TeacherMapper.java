package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;
import com.inf.daycare.models.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "userId", source = "user.id")
    GetTeacherDto teacherToGetTeacherDto(Teacher teacher);

    Teacher postTeacherDtoToTeacher(PostTeacherDto postTeacherDto);

    void updateTeacherFromPutTeacherDto(PutTeacherDto putTeacherDto, @MappingTarget Teacher teacher);
}
