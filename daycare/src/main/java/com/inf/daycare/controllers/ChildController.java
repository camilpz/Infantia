package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetChildDto;
import com.inf.daycare.dtos.post.PostChildDto;
import com.inf.daycare.dtos.put.PutChildDto;
import com.inf.daycare.services.ChildService;
import com.inf.daycare.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;
    private final AuthService authService;

    @GetMapping("getById/{child_id}")
    public ResponseEntity<GetChildDto> getById(@PathVariable Long child_id) {
        var childDTO = childService.getById(child_id);

        return ResponseEntity.ok().body(childDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetChildDto>> getAllChildren() {
        var childrenDTO = childService.getAllChildren();

        return ResponseEntity.ok().body(childrenDTO);
    }

    @GetMapping("/getAllByStatus/{enabled}")
    public ResponseEntity<List<GetChildDto>> getAllChildrenByStatus(@PathVariable Boolean enabled) {
        var childrenDTO = childService.getAllChildrenByStatus(enabled);

        return ResponseEntity.ok().body(childrenDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<GetChildDto> create(@RequestBody PostChildDto postChildDto) {
        // Obtener el ID del tutor directamente del usuario logueado
        Long tutorId = authService.getLoggedInTutorId();

        var childDTO = childService.create(postChildDto, tutorId);

        return ResponseEntity.ok().body(childDTO);
    }

    @PutMapping("/update/{child_id}")
    public ResponseEntity<GetChildDto> update(@PathVariable Long child_id, @RequestBody PutChildDto putChildDto) {

        var childDTO = childService.edit(child_id, putChildDto);

        return ResponseEntity.ok().body(childDTO);
    }

    @DeleteMapping("/disable/{child_id}/{tutor_id}")
    public ResponseEntity<Void> disable(@PathVariable Long child_id, @PathVariable Long tutor_id) {

        childService.disableChild(child_id, tutor_id);

        return ResponseEntity.noContent().build();
    }
}
