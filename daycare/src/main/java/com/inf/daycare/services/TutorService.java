package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.get.GetTutorDto;
import com.inf.daycare.dtos.post.PostTutorDto;
import com.inf.daycare.dtos.put.PutTutorDto;
import com.inf.daycare.models.Tutor;
import com.inf.daycare.models.User;

import java.util.List;

public interface TutorService {
    Tutor create(PostTutorDto postTutorDto, User user);
    GetTutorDto edit(Long tutorId, PutTutorDto putTutorDto);
    GetTutorDto getTutorById(Long tutorId);
    GetTutorDto disableTutor(Long tutorId);
    List<GetChildDto> getAllChildrensByTutorId(Long tutorId);
    Tutor getTutorOrThrow(Long tutorId);
}
