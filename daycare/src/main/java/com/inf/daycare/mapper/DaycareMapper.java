package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.models.Daycare;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DaycareMapper {
    //Daycare to GetDaycareDto
    GetDaycareDto daycareToGetDaycareDto(Daycare daycare);

    //PostDaycareDto to Daycare
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "validated", ignore = true)
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    Daycare postDaycareDtoToDaycare(PostDaycareDto dto);

    //Update Daycare from PutDaycareDto
    void updateDaycareFromPutDaycareDto(PutDaycareDto putDaycareDto, @MappingTarget Daycare daycare);

    List<GetDaycareDto> daycaresToGetDaycareDtos(List<Daycare> daycares);
}
