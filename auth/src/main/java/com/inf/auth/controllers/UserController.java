package com.inf.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
//    private final UserService userService;

    @GetMapping("/getById")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("User details");
    }
}
