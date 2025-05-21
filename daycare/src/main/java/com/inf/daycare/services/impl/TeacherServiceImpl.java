package com.inf.daycare.services.impl;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;
import com.inf.daycare.mapper.TeacherMapper;
import com.inf.daycare.models.Teacher;
import com.inf.daycare.repositories.TeacherRepository;
import com.inf.daycare.services.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public GetTeacherDto getById(Long teacherId) {
        Teacher teacher = getTeacherOrThrow(teacherId);

        return teacherMapper.teacherToGetTeacherDto(teacher);
    }

    @Override
    public GetTeacherDto getByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado con el ID de usuario: " + userId));

        return teacherMapper.teacherToGetTeacherDto(teacher);
    }

    @Override
    public GetTeacherDto create(PostTeacherDto postTeacherDto) {
        Teacher teacher = teacherMapper.postTeacherDtoToTeacher(postTeacherDto);

        Teacher savedTeacher = teacherRepository.save(teacher);

        return teacherMapper.teacherToGetTeacherDto(savedTeacher);
    }

    @Override
    public GetTeacherDto update(Long teacherId, PutTeacherDto putTeacherDto) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        teacherMapper.updateTeacherFromPutTeacherDto(putTeacherDto, teacher);

        teacherRepository.save(teacher);

        return teacherMapper.teacherToGetTeacherDto(teacher);
    }

    @Override
    public void disable(Long teacherId) {
        Teacher teacher = getTeacherOrThrow(teacherId);
        teacher.setEnabled(false);

        teacherRepository.save(teacher);
    }

    public Teacher getTeacherOrThrow(Long teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Profesor no encontrado"));
    }
}
