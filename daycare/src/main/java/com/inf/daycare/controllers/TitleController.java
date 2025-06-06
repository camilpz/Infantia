package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetTitleDto;
import com.inf.daycare.services.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/title")
@RequiredArgsConstructor
public class TitleController {
    private final TitleService titleService;

    @GetMapping("/getAll")
    public ResponseEntity<List<GetTitleDto>> getAllTitles() {
        List<GetTitleDto> titles = titleService.getAllTitles();
        return ResponseEntity.ok(titles);
    }
}
