package com.inf.daycare.services;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;
import com.inf.daycare.models.Teacher;
import com.inf.daycare.models.User;

public interface TeacherService {
    GetTeacherDto getById(Long teacherId);
    GetTeacherDto getByUserId(Long userId);
    Teacher create(PostTeacherDto postTeacherDto, User user);
    GetTeacherDto update(Long teacherId, PutTeacherDto putTeacherDto);
    void disable(Long teacherId);

    Teacher getTeacherOrThrow(Long teacherId);
}
