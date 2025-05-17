package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;

import java.util.List;

public interface DaycareService {
    GetDaycareDto getById(Long daycareId);
    List<GetDaycareDto> getAllDaycaresByDirectorId(Long directorId);
    GetDaycareDto create(PostDaycareDto postDaycareDto);
    GetDaycareDto update(Long daycareId, PutDaycareDto putDaycareDto);
    void disable(Long daycareId);
}
