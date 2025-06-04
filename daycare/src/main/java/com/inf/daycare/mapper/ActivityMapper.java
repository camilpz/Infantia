package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetActivityDto;
import com.inf.daycare.dtos.post.PostActivityDto;
import com.inf.daycare.dtos.put.PutActivityDto;
import com.inf.daycare.models.Activity;
import com.inf.daycare.models.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classroom", ignore = true) // Se setea en el servicio
    Activity postActivityDtoToActivity(PostActivityDto postActivityDto);

    GetActivityDto activityToGetActivityDto(Activity activity);
    List<GetActivityDto> activityToGetActivityDto(Set<Activity> activities);
    List<GetActivityDto> activityToGetActivityDto(List<Activity> activities);

    //Update Activity from PutActivityDto
    void updateActivityFromPutActivityDto(PutActivityDto putActivityDto, @MappingTarget Activity activity);
}
