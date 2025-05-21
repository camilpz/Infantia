package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.models.Daycare;

import java.util.List;

public interface DaycareService {
    GetDaycareDto getById(Long daycareId);
    List<GetDaycareDto> getAllDaycares();
    List<GetDaycareDto> getAllDaycaresByDirectorId(Long directorId);
    GetDaycareDto create(PostDaycareDto postDaycareDto);
    GetDaycareDto update(Long daycareId, PutDaycareDto putDaycareDto);
    void disable(Long daycareId);

    Boolean addTeacherToDaycare(Long daycareId, Long teacherId);

    Daycare getDaycareOrThrow(Long daycareId);
}
