package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetContactTypeDto;
import com.inf.daycare.services.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-type")
@RequiredArgsConstructor
public class ContactTypeController {

    private final ContactTypeService contactTypeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<GetContactTypeDto>> getAllDocumentTypes(@RequestParam(required = false) String name) {
        List<GetContactTypeDto> contactTypes = contactTypeService.getAllContactTypes(name);

        return ResponseEntity.ok().body(contactTypes);
    }
}
