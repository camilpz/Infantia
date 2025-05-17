package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;
import com.inf.daycare.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/getById/{teacherId}")
    public ResponseEntity<GetTeacherDto> getById(@PathVariable Long teacherId) {
        GetTeacherDto teacher = teacherService.getById(teacherId);
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<GetTeacherDto> getByUserId(@PathVariable Long userId) {
        GetTeacherDto teacher = teacherService.getByUserId(userId);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/create")
    public ResponseEntity<GetTeacherDto> create(@RequestBody PostTeacherDto postTeacherDto) {
        GetTeacherDto createdTeacher = teacherService.create(postTeacherDto);
        return ResponseEntity.ok(createdTeacher);
    }

    @PutMapping("/update/{teacherId}")
    public ResponseEntity<GetTeacherDto> update(@PathVariable Long teacherId, @RequestBody PutTeacherDto putTeacherDto) {
        GetTeacherDto updatedTeacher = teacherService.update(teacherId, putTeacherDto);
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/disable/{teacherId}")
    public ResponseEntity<Void> disable(@PathVariable Long teacherId) {
        teacherService.disable(teacherId);
        return ResponseEntity.noContent().build();
    }
}
