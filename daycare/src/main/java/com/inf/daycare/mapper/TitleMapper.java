package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetTitleDto;
import com.inf.daycare.models.Title;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TitleMapper {
    // Mapea una entidad Title a su DTO
    GetTitleDto titleToGetTitleDto(Title title);

    // Mapea una lista de entidades Title a una lista de DTOs
    List<GetTitleDto> titleListToGetTitleDtoList(List<Title> titles);

    // Si necesitas mapear de DTO a Entidad (ej. si permites crear títulos desde el front)
    // Title getTitleDtoToTitle(GetTitleDto getTitleDto);
}
