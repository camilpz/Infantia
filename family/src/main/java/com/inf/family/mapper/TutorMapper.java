package com.inf.family.mapper;

import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.dtos.get.GetTutorDto;
import com.inf.family.models.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorMapper {

    @Mapping(target = "id", ignore = true) //Porque al crear no tiene ID
    Tutor postTutorDtoToTutor(PostTutorDto postTutorDto);


    GetTutorDto tutorToGetTutorDto(Tutor tutor);

    // Mapea valores del DTO sobre el Tutor existente
    @Mapping(target = "id", ignore = true) //No tocar el ID
    void updateTutorFromPostTutorDto(PostTutorDto dto, @MappingTarget Tutor tutor);
}
