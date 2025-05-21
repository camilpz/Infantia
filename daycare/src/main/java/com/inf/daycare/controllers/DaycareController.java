package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetDaycareDto;
import com.inf.daycare.dtos.post.PostDaycareDto;
import com.inf.daycare.dtos.put.PutDaycareDto;
import com.inf.daycare.services.DaycareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daycare")
@RequiredArgsConstructor
public class DaycareController {
    private final DaycareService daycareService;

    @GetMapping("/getById/{daycareId}")
    public ResponseEntity<GetDaycareDto> getById(@PathVariable Long daycareId) {
        GetDaycareDto daycare = daycareService.getById(daycareId);
        return ResponseEntity.ok(daycare);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetDaycareDto>> getAll() {
        List<GetDaycareDto> daycares = daycareService.getAllDaycares();
        return ResponseEntity.ok(daycares);
    }

    @GetMapping("/getAllByDirectorId/{directorId}")
    public ResponseEntity<List<GetDaycareDto>> getAllByDirectorId(@PathVariable Long directorId) {
        List<GetDaycareDto> daycares = daycareService.getAllDaycaresByDirectorId(directorId);
        return ResponseEntity.ok(daycares);
    }

    @PostMapping("/create")
    public ResponseEntity<GetDaycareDto> create(@RequestBody PostDaycareDto postDaycareDto) {
        GetDaycareDto createdDaycare = daycareService.create(postDaycareDto);
        return ResponseEntity.ok(createdDaycare);
    }

    @PutMapping("/update/{daycareId}")
    public ResponseEntity<GetDaycareDto> update(@PathVariable Long daycareId, @RequestBody PutDaycareDto putDaycareDto) {
        GetDaycareDto updatedDaycare = daycareService.update(daycareId, putDaycareDto);
        return ResponseEntity.ok(updatedDaycare);
    }

    //Revisar
    @DeleteMapping("/disable/{daycareId}")
    public ResponseEntity<Void> disable(@PathVariable Long daycareId) {
        daycareService.disable(daycareId);
        return ResponseEntity.noContent().build();
    }
}
