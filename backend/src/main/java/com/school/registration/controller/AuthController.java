package com.school.registration.controller;

import com.school.registration.dto.auth.AuthResponse;
import com.school.registration.dto.auth.LoginRequest;
import com.school.registration.dto.auth.ProfileResponse;
import com.school.registration.dto.auth.RegisterRequest;
import com.school.registration.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public ProfileResponse me() {
        return authService.getProfile();
    }
}
