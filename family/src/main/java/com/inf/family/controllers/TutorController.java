package com.inf.family.controllers;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.get.GetTutorDto;
import com.inf.family.dtos.post.PostTutorDto;
import com.inf.family.services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @PostMapping("/create")
    public ResponseEntity<GetTutorDto> create(@RequestBody PostTutorDto postTutorDto) {
        var tutorDTO = tutorService.create(postTutorDto);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GetTutorDto> update(@PathVariable Long id, @RequestBody PostTutorDto postTutorDto) {
        var tutorDTO = tutorService.edit(id, postTutorDto);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetTutorDto> getById(@PathVariable Long id) {
        var tutorDTO = tutorService.getTutorById(id);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<GetTutorDto> enable(@PathVariable Long id) {
        var tutorDTO = tutorService.deleteTutor(id);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @GetMapping("/getAllChildren/{id}")
    public ResponseEntity<List<GetChildDto>> getAllChildren(@PathVariable Long id) {
        var tutorDTO = tutorService.getAllChildrensByTutorId(id);

        return ResponseEntity.ok().body(tutorDTO);
    }
}
