package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetEnrollmentDto;
import com.inf.daycare.dtos.get.GetSingleEnrollmentDto;
import com.inf.daycare.dtos.post.PostEnrollmentDto;
import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.StatusEnum;

import java.util.List;

public interface EnrollmentService {
    GetSingleEnrollmentDto getById(Long enrollmentId);
    List<GetEnrollmentDto> getAllByDaycareId(Long daycareId);
    GetSingleEnrollmentDto create(PostEnrollmentDto postEnrollmentDto);
    GetSingleEnrollmentDto updateStatus(Long enrollmentId, StatusEnum status);
    GetSingleEnrollmentDto updatePaymentStatus(Long enrollmentId, PayStatusEnum paymentStatus);

    //------------------------------------

}
