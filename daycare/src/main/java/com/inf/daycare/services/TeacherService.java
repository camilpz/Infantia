package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;

public interface TeacherService {
    GetTeacherDto getById(Long teacherId);
    GetTeacherDto getByUserId(Long userId);
    GetTeacherDto create(PostTeacherDto postTeacherDto);
    GetTeacherDto update(Long teacherId, PutTeacherDto putTeacherDto);
    void disable(Long teacherId);
}
