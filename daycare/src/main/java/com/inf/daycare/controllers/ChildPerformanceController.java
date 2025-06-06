package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetChildPerformanceDto;
import com.inf.daycare.dtos.post.PostChildPerformanceDto;
import com.inf.daycare.dtos.put.PutChildPerformaneDto;
import com.inf.daycare.enums.DevelopmentAreaEnum;
import com.inf.daycare.services.ChildPerformanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/childPerformance")
@RequiredArgsConstructor
public class ChildPerformanceController {
    private final ChildPerformanceService childPerformanceService;

    @PostMapping
    public ResponseEntity<GetChildPerformanceDto> createChildPerformance(@Valid @RequestBody PostChildPerformanceDto postChildPerformanceDto) {
        GetChildPerformanceDto createdPerformance = childPerformanceService.createChildPerformance(postChildPerformanceDto);
        return ResponseEntity.ok(createdPerformance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetChildPerformanceDto> updateChildPerformance(@PathVariable Long id, @Valid @RequestBody PutChildPerformaneDto putChildPerformanceDto) {
        GetChildPerformanceDto updatedPerformance = childPerformanceService.updateChildPerformance(id, putChildPerformanceDto);
        return ResponseEntity.ok(updatedPerformance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChildPerformance(@PathVariable Long id) {
        childPerformanceService.deleteChildPerformance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetChildPerformanceDto> getChildPerformanceById(@PathVariable Long id) {
        GetChildPerformanceDto performance = childPerformanceService.getChildPerformanceById(id);
        return ResponseEntity.ok(performance);
    }

    @GetMapping
    public ResponseEntity<List<GetChildPerformanceDto>> getAllChildPerformances() {
        List<GetChildPerformanceDto> performances = childPerformanceService.getAllChildPerformances();
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<GetChildPerformanceDto>> getChildPerformancesByChildId(@PathVariable Long childId) {
        List<GetChildPerformanceDto> performances = childPerformanceService.getChildPerformancesByChildId(childId);
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/child/{childId}/area/{area}")
    public ResponseEntity<List<GetChildPerformanceDto>> getChildPerformancesByChildIdAndDevelopmentArea(
            @PathVariable Long childId,
            @PathVariable DevelopmentAreaEnum area) {
        List<GetChildPerformanceDto> performances = childPerformanceService.getChildPerformancesByChildIdAndDevelopmentArea(childId, area);
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/teacher/{teacherUserId}")
    public ResponseEntity<List<GetChildPerformanceDto>> getChildPerformancesByTeacherId(@PathVariable Long teacherUserId) {
        List<GetChildPerformanceDto> performances = childPerformanceService.getChildPerformancesByTeacherId(teacherUserId);
        return ResponseEntity.ok(performances);
    }

    @GetMapping("/date/{date}") // Formato esperado para 'date': AAAA-MM-DD
    public ResponseEntity<List<GetChildPerformanceDto>> getChildPerformancesByDate(@PathVariable LocalDate date) {
        List<GetChildPerformanceDto> performances = childPerformanceService.getChildPerformancesByDate(date);
        return ResponseEntity.ok(performances);
    }
}
