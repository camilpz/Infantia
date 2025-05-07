package com.inf.users.controllers;

import com.inf.users.dtos.get.GetUserDto;
import com.inf.users.dtos.post.LoginDto;
import com.inf.users.dtos.post.PostUserTutorDto;
import com.inf.users.dtos.put.PutUserTutorDto;
import com.inf.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<GetUserDto> createUser(@RequestBody PostUserTutorDto postUserTutorDto) {
        // Logic to create a user

        var userDTO = userService.createUserTutor(postUserTutorDto);

        System.out.println("userDTO: " + userDTO);

        return ResponseEntity.ok().body(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<GetUserDto> loginUser(@RequestBody LoginDto loginDto) {
        // Logic to log in a user

        var userDTO = userService.login(loginDto);

        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<GetUserDto>> getAllUsers() {
        // Logic to get all users
        var users = userService.getAllUsers();

        return ResponseEntity.ok().body(users);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<GetUserDto> updateTutor(@PathVariable Long userId, @RequestBody PutUserTutorDto putUserTutorDto) {

        var updatedUser = userService.editTutor(userId, putUserTutorDto);

        return ResponseEntity.ok().body(updatedUser);
    }
}
