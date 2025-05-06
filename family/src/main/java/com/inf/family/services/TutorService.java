package com.inf.family.services;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.dtos.get.GetTutorDto;

import java.util.List;

public interface TutorService {
    GetTutorDto create(PostTutorDto postTutorDto);
    GetTutorDto edit(Long tutorId, PostTutorDto postTutorDto);
    GetTutorDto getTutorById(Long tutorId);
    GetTutorDto deleteTutor(Long tutorId);
    List<GetChildDto> getAllChildrensByTutorId(Long tutorId);
}
