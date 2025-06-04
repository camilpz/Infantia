package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetContactTypeDto;
import com.inf.daycare.models.ContactType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactTypeMapper {

    default String mapContactTypeToString(ContactType contactType) {
        return contactType != null ? contactType.getName() : null;
    }

    //ContactType -> GetContactTypeDto
    List<GetContactTypeDto> contactTypeListToGetContactTypeDtoList(List<ContactType> contactTypes);
}
