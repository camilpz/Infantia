package com.inf.family.controllers;

import com.inf.family.dtos.get.GetChildDto;
import com.inf.family.dtos.post.PostChildDto;
import com.inf.family.services.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping("/create/{tutor_id}")
    public ResponseEntity<GetChildDto> create(@PathVariable Long tutor_id, @RequestBody PostChildDto postChildDto) {

        var childDTO = childService.create(postChildDto, tutor_id);

        return ResponseEntity.ok().body(childDTO);
    }

    @PutMapping("/edit/{child_id}")
    public ResponseEntity<GetChildDto> edit(@PathVariable Long child_id, @RequestBody PostChildDto postChildDto) {

        var childDTO = childService.edit(child_id, postChildDto);

        return ResponseEntity.ok().body(childDTO);
    }

//    @GetMapping("/get/{child_id}")

    @GetMapping("/getAllByTutor/{tutor_id}")
    public ResponseEntity<List<GetChildDto>> getAllByTutor(@PathVariable Long tutor_id) {

        var childDTO = childService.getAllByTutorId(tutor_id);

        return ResponseEntity.ok().body(childDTO);
    }

    @DeleteMapping("/delete/{child_id}/{tutor_id}")
    public ResponseEntity<Void> delete(@PathVariable Long child_id, @PathVariable Long tutor_id) {

        childService.deleteChild(child_id, tutor_id);

        return ResponseEntity.noContent().build();
    }
}
