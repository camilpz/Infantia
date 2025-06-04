package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetClassroomDto;
import com.inf.daycare.dtos.post.PostClassroomDto;
import com.inf.daycare.dtos.put.PutClassroomDto;
import com.inf.daycare.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping("/getById/{classroomId}")
    public ResponseEntity<GetClassroomDto> getClassroomById(@PathVariable Long classroomId) {
        GetClassroomDto classroom = classroomService.getById(classroomId);
        return ResponseEntity.ok(classroom);
    }

    @GetMapping("getAllByDaycare/{daycareId}")
    public ResponseEntity<List<GetClassroomDto>> getAllByDaycare(@PathVariable Long daycareId) {
        List<GetClassroomDto> classrooms = classroomService.getAllByDaycareId(daycareId);
        return ResponseEntity.ok(classrooms);
    }

    @PostMapping("/create")
    public ResponseEntity<GetClassroomDto> create(@RequestBody PostClassroomDto postClassroomDto) {
        GetClassroomDto classroom = classroomService.create(postClassroomDto);
        return ResponseEntity.ok(classroom);
    }

    @PutMapping("/update/{classroomId}")
    public ResponseEntity<GetClassroomDto> update(@PathVariable Long classroomId, @RequestBody PutClassroomDto putClassroomDto) {
        GetClassroomDto classroom = classroomService.update(classroomId, putClassroomDto);
        return ResponseEntity.ok(classroom);
    }

    @DeleteMapping("/disable/{classroomId}")
    public ResponseEntity<Void> disable(@PathVariable Long classroomId) {
        classroomService.disableClassroom(classroomId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/changeStatus/{classroomId}")
    public ResponseEntity<Void> enable(@PathVariable Long classroomId, @RequestParam boolean status) {
        classroomService.changeStatus(classroomId, status);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addTeacher/{classroomId}/{teacherId}/{isPrincipal}")
    public ResponseEntity<Void> addTeacher(@PathVariable Long classroomId, @PathVariable Long teacherId, @PathVariable Boolean isPrincipal) {
        classroomService.addTeacherToClassroom(classroomId, teacherId, isPrincipal);
        return ResponseEntity.noContent().build();
    }
}
