package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetAuthorizedPersonDto;
import com.inf.daycare.dtos.post.PostAuthorizedPersonDto;
import com.inf.daycare.dtos.put.PutAuthorizedPersonDto;
import com.inf.daycare.models.AuthorizedPerson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorizedPersonMapper {
    PostAuthorizedPersonDto authorizedPersonToPostAuthorizedPersonDto(AuthorizedPerson authorizedPerson);
    AuthorizedPerson postAuthorizedPersonDtoToAuthorizedPerson(PostAuthorizedPersonDto dto);


    // Put DTO to Entity (para actualizar)
    // Los campos nulos en PutAuthorizedPersonDto no sobrescribirán los valores existentes por defecto con MapStruct
    @Mapping(target = "id", ignore = true) // ID no se actualiza desde el DTO
    void updateAuthorizedPersonFromPutDto(PutAuthorizedPersonDto dto, @MappingTarget AuthorizedPerson entity);

    // Entity to Get DTO (para obtener)
    GetAuthorizedPersonDto authorizedPersonToGetAuthorizedPersonDto(AuthorizedPerson authorizedPerson);

    // Lista de entidades a lista de Get DTOs
    List<GetAuthorizedPersonDto> listAuthorizedPersonToGetAuthorizedPersonDtoList(List<AuthorizedPerson> authorizedPeople);
}
