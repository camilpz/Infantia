package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.post.PostChildDto;
import com.inf.daycare.dtos.put.PutChildDto;
import com.inf.daycare.models.Child;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
