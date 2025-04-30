package com.inf.auth.controllers;

import com.inf.auth.dtos.AuthResponseDto;
import com.inf.auth.dtos.CustomUserDetails;
import com.inf.auth.dtos.LoginDto;
import com.inf.auth.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDto(token));
   }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register() {
//        // Implement your registration logic here
//        return ResponseEntity.ok("Registration successful");
//    }
//
//    @PostMapping("/validate-token")
//    public ResponseEntity<Boolean> validateToken() {
//        // Implement your token validation logic here
//        return ResponseEntity.ok(true);
//    }
}
