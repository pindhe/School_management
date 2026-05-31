package com.school.sms.controller;

import com.school.sms.dto.auth.AuthResponse;
import com.school.sms.dto.auth.ChangePasswordRequest;
import com.school.sms.dto.auth.LoginRequest;
import com.school.sms.dto.common.MessageResponse;
import com.school.sms.dto.user.UpdateProfileRequest;
import com.school.sms.dto.user.UserResponse;
import com.school.sms.service.AuthService;
import com.school.sms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login, profile and password endpoints")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate and obtain a JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Get the currently authenticated user")
    public ResponseEntity<UserResponse> me() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @PutMapping("/me")
    @Operation(summary = "Update own profile")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateOwnProfile(request));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change own password")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }
}
