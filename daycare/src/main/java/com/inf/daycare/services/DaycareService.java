package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareShiftDefinitionDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.enums.ShiftEnum;
import com.inf.daycare.models.Daycare;
import com.inf.daycare.models.DaycareShiftDefinition;

import java.util.List;

public interface DaycareService {
    GetDaycareDto getById(Long daycareId);
    List<GetDaycareDto> getAllDaycares();
    List<GetDaycareDto> getAllDaycaresByDirectorId(Long directorId);
    GetDaycareDto create(PostDaycareDto postDaycareDto, Long directorId);
    GetDaycareDto update(Long daycareId, PutDaycareDto putDaycareDto);
    void disable(Long daycareId);
    void enable(Long daycareId);
    void validateDaycare(Long daycareId);


    void addTeacherToDaycare(Long daycareId, Long teacherId);
    void removeTeacherFromDaycare(Long daycareId, Long teacherId, Long directorId);

    DaycareShiftDefinition createOrUpdateShiftDefinition(Long daycareId, PostDaycareShiftDefinitionDto dto, Long directorId);

    Daycare getDaycareOrThrow(Long daycareId);
    DaycareShiftDefinition getDaycareShiftDefinitionOrThrow(Daycare daycare, ShiftEnum shiftType);
}
