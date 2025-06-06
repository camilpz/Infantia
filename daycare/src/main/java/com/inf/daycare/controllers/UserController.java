package com.inf.daycare.controllers;

import com.inf.daycare.dtos.get.GetUserDto;
import com.inf.daycare.dtos.post.PostUserDto;
import com.inf.daycare.dtos.put.PutUserDto;
import com.inf.daycare.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getById/{userId}")
    public ResponseEntity<GetUserDto> getUserById(@PathVariable Long userId) {
        var userDTO = userService.getUserById(userId);

        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetUserDto>> getAllUsers() {
        var users = userService.getAllUsers();

        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/create")
    public ResponseEntity<GetUserDto> createUser(@RequestBody PostUserDto postUserDto) {
        var userDTO = userService.createUser(postUserDto);

        return ResponseEntity.ok().body(userDTO);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<GetUserDto> updateUser(@PathVariable Long userId, @RequestBody PutUserDto putUserDto) {
        var updatedUser = userService.editUser(userId, putUserDto);

        return ResponseEntity.ok().body(updatedUser);
    }

    @GetMapping("/emailIsAvailable")
    public ResponseEntity<Boolean> isEmailAvailable(@RequestParam String email) {
        boolean isAvailable = userService.emailIsAvailable(email);
        return ResponseEntity.ok(isAvailable);
    }
}
