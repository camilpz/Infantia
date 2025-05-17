package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.services.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @GetMapping("/getById/{directorId}")
    public ResponseEntity<GetDirectorDto> getById(@PathVariable Long directorId) {
        GetDirectorDto directorDto = directorService.getById(directorId);
        return ResponseEntity.ok(directorDto);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<GetDirectorDto> getByUserId(@PathVariable Long userId) {
        GetDirectorDto directorDto = directorService.getByUserId(userId);
        return ResponseEntity.ok(directorDto);
    }

    @PostMapping("/create")
    public ResponseEntity<GetDirectorDto> create(@RequestBody PostDirectorDto postDirectorDto) {
        GetDirectorDto createdDirector = directorService.create(postDirectorDto);
        return ResponseEntity.ok(createdDirector);
    }

    @PutMapping("/update/{directorId}")
    public ResponseEntity<GetDirectorDto> update(@PathVariable Long directorId, @RequestBody PutDirectorDto putDirectorDto) {
        GetDirectorDto updatedDirector = directorService.update(directorId, putDirectorDto);
        return ResponseEntity.ok(updatedDirector);
    }

    @DeleteMapping("/disable/{directorId}")
    public ResponseEntity<Void> disable(@PathVariable Long directorId) {
        directorService.disable(directorId);
        return ResponseEntity.noContent().build();
    }
}
