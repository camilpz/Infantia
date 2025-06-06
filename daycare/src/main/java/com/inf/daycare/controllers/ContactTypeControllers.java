package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetContactTypeDto;
import com.inf.daycare.services.ContactTypeService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contactType")
@RequiredArgsConstructor
public class ContactTypeControllers {

    private final ContactTypeService contactTypeService;

@GetMapping("/getAll")
public ResponseEntity<List<GetContactTypeDto>> getAllContactTypes(
        @org.springframework.web.bind.annotation.RequestParam(value = "name", required = false) String name) {
    List<GetContactTypeDto> contactTypes = contactTypeService.getAllContactTypes(name);
    return ResponseEntity.ok(contactTypes);
}
}
