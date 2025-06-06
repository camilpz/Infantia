package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetChildPerformanceDto;
import com.inf.daycare.dtos.post.PostChildPerformanceDto;
import com.inf.daycare.dtos.put.PutChildPerformaneDto;
import com.inf.daycare.enums.AchievementLevelEnum;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import com.inf.daycare.models.ChildPerformance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChildPerformanceMapper {
    // Mapeo de Post DTO a Entidad (para crear)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "child", ignore = true) // La entidad Child se establece en el servicio
    @Mapping(target = "teacher", ignore = true) // La entidad User (teacher) se establece en el servicio
    @Mapping(target = "createdAt", ignore = true) // Se autogenera en @PrePersist
    @Mapping(target = "evaluationDate", source = "evaluationDate", defaultValue = "java(java.time.LocalDate.now())") // Usar fecha actual si no se provee
    ChildPerformance postChildPerformanceDtoToChildPerformance(PostChildPerformanceDto dto);

    // Mapeo de Put DTO a Entidad (para actualizar)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "child", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateChildPerformanceFromPutDto(PutChildPerformaneDto dto, @MappingTarget ChildPerformance entity);

    // Mapeo de Entidad a Get DTO (para obtener)
    @Mapping(target = "childId", source = "child.id")
    @Mapping(target = "childFirstName", source = "child.firstName")
    @Mapping(target = "childLastName", source = "child.lastName")
    @Mapping(target = "teacherUserId", source = "teacher.id")
    @Mapping(target = "developmentAreaDisplayName", expression = "java(childPerformance.getDevelopmentArea().getDisplayName())")
    @Mapping(target = "achievementLevelDisplayName", expression = "java(childPerformance.getAchievementLevel().getDisplayName())")
    GetChildPerformanceDto childPerformanceToGetChildPerformanceDto(ChildPerformance childPerformance);

    // Mapeo de lista de entidades a lista de Get DTOs
    List<GetChildPerformanceDto> listChildPerformanceToGetChildPerformanceDtoList(List<ChildPerformance> childPerformances);

    // Helper methods for enums display name (MapStruct will find them)
    default String mapDevelopmentAreaEnum(DevelopmentAreaEnum enumValue) {
        return enumValue != null ? enumValue.getDisplayName() : null;
    }

    default String mapAchievementLevelEnum(AchievementLevelEnum enumValue) {
        return enumValue != null ? enumValue.getDisplayName() : null;
    }
}
