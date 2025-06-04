package com.inf.daycare.mapper;

import com.inf.daycare.dtos.post.PostDaycareShiftDefinitionDto;
import com.inf.daycare.models.DaycareShiftDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DaycareShiftDefinitionMapper {
    // Mapeo para creación: de DTO a entidad
    // daycare se establecerá en el servicio, no aquí directamente
    @Mapping(target = "id", ignore = true) // El ID se genera automáticamente
    @Mapping(target = "daycare", ignore = true) // Se asigna manualmente en el servicio
    DaycareShiftDefinition postDtoToEntity(PostDaycareShiftDefinitionDto dto);

    // Mapeo para actualización: de DTO a entidad existente
    // @MappingTarget indica que 'entity' es el objeto a actualizar
    @Mapping(target = "id", ignore = true) // El ID no se actualiza
    @Mapping(target = "daycare", ignore = true) // La relación no se actualiza a través del DTO
    void updateEntityFromDto(PostDaycareShiftDefinitionDto dto, @MappingTarget DaycareShiftDefinition entity);
}
