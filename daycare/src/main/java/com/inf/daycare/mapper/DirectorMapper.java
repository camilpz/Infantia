package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.Director;
import com.inf.daycare.models.Title;
import com.inf.daycare.repositories.TitleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {TitleMapper.class})
public interface DirectorMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "titles", source = "titles")
    GetDirectorDto directorToGetDirectorDto(Director director);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "daycares", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    Director postDirectorDtoToDirector(PostDirectorDto postDirectorDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "daycares", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    void updateDirectorFromPutDirectorDto(PutDirectorDto putDirectorDto, @MappingTarget Director director);
//    @Mapping(target = "userId", source = "user.id")
//    GetDirectorDto directorToGetDirectorDto(Director director);
//
//    //PostDirectorDto to Director
//    Director postDirectorDtoToDirector(PostDirectorDto postDirectorDto);
//
//    //Update Director from PutDirectorDto
//    void updateDirectorFromPutDirectorDto(PutDirectorDto putDirectorDto, @MappingTarget Director director);


}
