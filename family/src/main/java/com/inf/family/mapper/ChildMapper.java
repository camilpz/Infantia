package com.inf.family.mapper;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostChildDto;
import com.inf.family.dtos.put.PutChildDto;
import com.inf.family.models.Child;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildMapper {

    //GetChildDto -> Child
    Child getChildDtoToChild(GetChildDto childDto);

    GetChildDto getChildToGetChildDto(Child child);

    //PostChildDto -> Child
    @Mapping(target = "id", ignore = true)
    Child postChildDtoToChild(PostChildDto childDto);

    List<GetChildDto> childListToGetChildDtoList(List<Child> children);

    @Mapping(target = "id", ignore = true)
    void updateChildFromPutChildDto(PutChildDto putChildDto, @MappingTarget Child child);

}
