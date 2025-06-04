package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.post.PostChildDto;
import com.inf.daycare.dtos.put.PutChildDto;
import com.inf.daycare.models.Child;
import com.inf.daycare.models.TutorChild;
import com.inf.daycare.models.TutorChildId;

import java.util.List;

public interface ChildService {
    GetChildDto getById(Long childId);
    List<GetChildDto> getAllChildren();
    List<GetChildDto> getAllChildrenByStatus(Boolean enabled);
    GetChildDto create(PostChildDto postChildDto, Long tutorId);
    GetChildDto edit(Long id, PutChildDto putChildDto);
    //GetChildDto deleteChild(Long childId, Long tutorId);
    GetChildDto disableChild(Long childId, Long tutorId);
    TutorChild getTutorChildOrThrow(TutorChildId id);
    Child getChildOrThrow(Long id);
    Integer calculateChildAge(Child child);
    boolean isChildAssociatedWithTutor(Long childId, Long tutorId);
}
