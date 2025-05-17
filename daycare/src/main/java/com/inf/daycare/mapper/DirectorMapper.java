package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.models.Director;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DirectorMapper {

    //Director to GetDirectorDto
    GetDirectorDto directorToGetDirectorDto(Director director);

    //PostDirectorDto to Director
    Director postDirectorDtoToDirector(PostDirectorDto postDirectorDto);

    //Update Director from PutDirectorDto
    void updateDirectorFromPutDirectorDto(PutDirectorDto putDirectorDto, @MappingTarget Director director);
}
