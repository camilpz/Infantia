package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetEnrollmentDto;
import com.inf.daycare.dtos.get.GetSingleEnrollmentDto;
import com.inf.daycare.dtos.post.PostEnrollmentDto;
import com.inf.daycare.enums.PayStatusEnum;
import com.inf.daycare.enums.StatusEnum;
import com.inf.daycare.services.EnrollmentService;
import com.inf.daycare.services.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final AuthService authService;

    private Long getCurrentTutorId() {
        return authService.getLoggedInTutorId();
    }

    @GetMapping("/getById/{enrollmentId}")
    public ResponseEntity<GetSingleEnrollmentDto> getById(@PathVariable Long enrollmentId) {
        GetSingleEnrollmentDto getSingleEnrollmentDto = enrollmentService.getById(enrollmentId);
        return ResponseEntity.ok(getSingleEnrollmentDto);
    }

    @GetMapping("/getAllByDaycareId/{daycareId}")
    public ResponseEntity<List<GetEnrollmentDto>> getAllByDaycareId(@PathVariable Long daycareId) {
        List<GetEnrollmentDto> getEnrollmentDtos = enrollmentService.getAllByDaycareId(daycareId);
        return ResponseEntity.ok(getEnrollmentDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<GetSingleEnrollmentDto> create(@RequestBody PostEnrollmentDto postEnrollmentDto) {
        GetSingleEnrollmentDto getSingleEnrollmentDto = enrollmentService.create(postEnrollmentDto, getCurrentTutorId());
        return ResponseEntity.ok(getSingleEnrollmentDto);
    }

    @PutMapping("/updateStatus/{enrollmentId}/{status}")
    public ResponseEntity<GetSingleEnrollmentDto> updateStatus(@PathVariable Long enrollmentId, @PathVariable StatusEnum status) {
        GetSingleEnrollmentDto getSingleEnrollmentDto = enrollmentService.updateStatus(enrollmentId, status);
        return ResponseEntity.ok(getSingleEnrollmentDto);
    }

    @PutMapping("/updatePaymentStatus/{enrollmentId}/{paymentStatus}")
    public ResponseEntity<GetSingleEnrollmentDto> updatePaymentStatus(@PathVariable Long enrollmentId, @PathVariable PayStatusEnum paymentStatus) {
        GetSingleEnrollmentDto getSingleEnrollmentDto = enrollmentService.updatePaymentStatus(enrollmentId, paymentStatus);
        return ResponseEntity.ok(getSingleEnrollmentDto);
    }
}
