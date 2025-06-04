package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetTutorDto;
import com.inf.daycare.dtos.post.PostTutorDto;
import com.inf.daycare.dtos.put.PutTutorDto;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorMapper {

    @Mapping(target = "id", ignore = true) //Porque al crear no tiene ID
    Tutor postTutorDtoToTutor(PostTutorDto postTutorDto);

    @Mapping(target = "id", source = "user.id") //Mapea el ID del usuario al ID del tutor
    GetTutorDto tutorToGetTutorDto(Tutor tutor);

    // Mapea valores del DTO sobre el Tutor existente
    @Mapping(target = "id", ignore = true) //No tocar el ID
    void updateTutorFromPutTutorDto(PutTutorDto dto, @MappingTarget Tutor tutor);

    default Long mapId(Tutor tutor) {
        return tutor.getUser().getId();
    }
}
