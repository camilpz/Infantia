package com.inf.daycare.mapper;

import com.inf.daycare.dtos.get.GetEnrollmentDto;
import com.inf.daycare.dtos.get.GetSingleEnrollmentDto;
import com.inf.daycare.dtos.post.PostEnrollmentDto;
import com.inf.daycare.models.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "daycare.id", target = "daycareId")
    GetSingleEnrollmentDto enrollmentToGetSimpleEnrollmentDto(Enrollment enrollment);

    //Enrollment -> GetEnrollmentDto
    GetEnrollmentDto enrollmentToGetEnrollmentDto(Enrollment enrollment);

    //List<Enrollment> -> List<GetEnrollmentDto>
    List<GetEnrollmentDto> listEnrollmentToGetEnrollmentDtoList(List<Enrollment> enrollments);

    //PostEnrollmentDto -> Enrollment
    Enrollment postEnrollmentDtoToEnrollment(PostEnrollmentDto postEnrollmentDto);
}
