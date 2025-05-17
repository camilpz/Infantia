package com.inf.family.services;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.get.GetTutorDto;
import com.inf.family.dtos.post.PostChildDto;
import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.dtos.put.PutChildDto;

import java.util.List;

public interface ChildService {
    GetChildDto create(PostChildDto postChildDto, Long tutorId);
    GetChildDto edit(Long id, PutChildDto putChildDto);
    List<GetChildDto> getAllByTutorId(Long tutorId);
    GetChildDto deleteChild(Long childId, Long tutorId);
}
