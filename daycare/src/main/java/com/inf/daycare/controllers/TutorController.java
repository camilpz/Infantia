package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.get.GetTutorDto;
import com.inf.daycare.dtos.put.PutTutorDto;
import com.inf.daycare.services.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutor")
@RequiredArgsConstructor
public class TutorController {

    private final TutorService tutorService;

    @PutMapping("/update/{id}")
    public ResponseEntity<GetTutorDto> update(@PathVariable Long id, @RequestBody PutTutorDto putTutorDto) {
        var tutorDTO = tutorService.edit(id, putTutorDto);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GetTutorDto> getById(@PathVariable Long id) {
        var tutorDTO = tutorService.getTutorById(id);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<GetTutorDto> enable(@PathVariable Long id) {
        var tutorDTO = tutorService.disableTutor(id);

        return ResponseEntity.ok().body(tutorDTO);
    }

    @GetMapping("/getAllChildren/{id}")
    public ResponseEntity<List<GetChildDto>> getAllChildren(@PathVariable Long id) {
        var tutorDTO = tutorService.getAllChildrensByTutorId(id);

        return ResponseEntity.ok().body(tutorDTO);
    }
}
