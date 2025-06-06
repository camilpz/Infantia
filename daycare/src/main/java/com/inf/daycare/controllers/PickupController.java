package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetPickupDto;
import com.inf.daycare.dtos.post.PostPickupDto;
import com.inf.daycare.services.PickupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pickup")
@RequiredArgsConstructor
public class PickupController {
    private final PickupService pickupService;

    @PostMapping("/create")
    public ResponseEntity<GetPickupDto> recordPickup(@Valid @RequestBody PostPickupDto postPickupDto) {
        GetPickupDto recordedPickup = pickupService.recordPickup(postPickupDto);
        return ResponseEntity.ok(recordedPickup);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetPickupDto>> getAllPickupRecords() {
        List<GetPickupDto> pickupRecords = pickupService.getAllPickupRecords();
        return ResponseEntity.ok(pickupRecords);
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<List<GetPickupDto>> getPickupRecordsByChild(@PathVariable Long childId) {
        List<GetPickupDto> pickupRecords = pickupService.getPickupRecordsByChildId(childId);
        return ResponseEntity.ok(pickupRecords);
    }

    @GetMapping("/date/{date}") // Formato de fecha esperado: YYYY-MM-DD
    public ResponseEntity<List<GetPickupDto>> getPickupRecordsByDate(@PathVariable LocalDate date) {
        List<GetPickupDto> pickupRecords = pickupService.getPickupRecordsByDate(date);
        return ResponseEntity.ok(pickupRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPickupDto> getPickupRecordById(@PathVariable Long id) {
        GetPickupDto pickupRecord = pickupService.getPickupRecordById(id);
        return ResponseEntity.ok(pickupRecord);
    }
}
