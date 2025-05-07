package com.inf.family.services;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.dtos.get.GetTutorDto;
import com.inf.family.dtos.put.PutTutorDto;

import java.util.List;

public interface TutorService {
    GetTutorDto create(PostTutorDto postTutorDto);
    GetTutorDto edit(Long tutorId, PutTutorDto putTutorDto);
    GetTutorDto getTutorById(Long tutorId);
    GetTutorDto deleteTutor(Long tutorId);
    List<GetChildDto> getAllChildrensByTutorId(Long tutorId);
}
