package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetPickupDto;
import com.inf.daycare.dtos.post.PostPickupDto;
import com.inf.daycare.models.Pickup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PickupMapper {
    // PostPickupRecordDto to PickupRecord (las relaciones se establecen en el servicio)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "child", ignore = true)
    @Mapping(target = "pickedUpBy", ignore = true)
    @Mapping(target = "attendance", ignore = true)
    Pickup postPickupDtoToPickup(PostPickupDto dto);

    // PickupRecord to GetPickupRecordDto
    @Mapping(target = "childId", source = "child.id")
    @Mapping(target = "childFirstName", source = "child.firstName")
    @Mapping(target = "childLastName", source = "child.lastName")
    @Mapping(target = "authorizedPersonId", source = "pickedUpBy.id")
    @Mapping(target = "authorizedPersonFirstName", source = "pickedUpBy.firstName")
    @Mapping(target = "authorizedPersonLastName", source = "pickedUpBy.lastName")
    @Mapping(target = "authorizedPersonRelationship", source = "pickedUpBy.relationshipToChild")
    @Mapping(target = "attendanceId", source = "attendance.id")
    GetPickupDto pickupToGetPickupDto(Pickup pickupRecord);

    List<GetPickupDto> listPickupToGetPickupDtoList(List<Pickup> pickupRecords);
}
