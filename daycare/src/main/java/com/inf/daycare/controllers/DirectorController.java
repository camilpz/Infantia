package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetDirectorDto;
import com.inf.daycare.dtos.post.PostDirectorDto;
import com.inf.daycare.dtos.put.PutDirectorDto;
import com.inf.daycare.services.DirectorService;
import com.inf.daycare.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/director")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;
    private final AuthService authService;

    private Long getCurrentDirectorId() {
        return authService.getLoggedInDirectorId();
    }

    @GetMapping("/getById/{directorId}")
    public ResponseEntity<GetDirectorDto> getById(@PathVariable Long directorId) {
        GetDirectorDto directorDto = directorService.getById(getCurrentDirectorId());
        return ResponseEntity.ok(directorDto);
    }

    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<GetDirectorDto> getByUserId(@PathVariable Long userId) {
        GetDirectorDto directorDto = directorService.getByUserId(userId);
        return ResponseEntity.ok(directorDto);
    }

    @PutMapping("/update")
    public ResponseEntity<GetDirectorDto> update(@RequestBody PutDirectorDto putDirectorDto) {
        GetDirectorDto updatedDirector = directorService.update(getCurrentDirectorId(), putDirectorDto);
        return ResponseEntity.ok(updatedDirector);
    }

    @DeleteMapping("/disable")
    public ResponseEntity<Void> disable() {
        directorService.disable(getCurrentDirectorId());
        return ResponseEntity.noContent().build();
    }
}
