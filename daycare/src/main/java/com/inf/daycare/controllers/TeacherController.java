package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetTeacherDto;
import com.inf.daycare.dtos.post.PostTeacherDto;
import com.inf.daycare.dtos.put.PutTeacherDto;
import com.inf.daycare.services.TeacherService;
import com.inf.daycare.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final AuthService authService;

    private Long getCurrentTeacherId() {
        return authService.getLoggedInTeacherId();
    }

    @GetMapping("/getById")
    public ResponseEntity<GetTeacherDto> getById() {
        GetTeacherDto teacher = teacherService.getById(getCurrentTeacherId());

        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<GetTeacherDto> getByUserId(@PathVariable Long userId) {
        GetTeacherDto teacher = teacherService.getByUserId(userId);
        return ResponseEntity.ok(teacher);
    }

    @PutMapping("/update")
    public ResponseEntity<GetTeacherDto> update(@RequestBody PutTeacherDto putTeacherDto) {
        GetTeacherDto updatedTeacher = teacherService.update(getCurrentTeacherId(), putTeacherDto);
        return ResponseEntity.ok(updatedTeacher);
    }

    //Deshabilita un profesor
    @DeleteMapping("/disable/{teacherId}")
    public ResponseEntity<Void> disable(@PathVariable Long teacherId) {
        teacherService.disable(teacherId);
        return ResponseEntity.noContent().build();
    }
}
