package com.school.registration.service;

import com.school.registration.domain.entity.AppUser;
import com.school.registration.domain.enums.RoleName;
import com.school.registration.dto.auth.AuthResponse;
import com.school.registration.dto.auth.LoginRequest;
import com.school.registration.dto.auth.ProfileResponse;
import com.school.registration.dto.auth.RegisterRequest;
import com.school.registration.exception.BadRequestException;
import com.school.registration.exception.ResourceNotFoundException;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.StudentRepository;
import com.school.registration.security.JwtService;
import com.school.registration.security.SecurityUtils;
import com.school.registration.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final SettingsService settingsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return buildAuthResponse(principal, jwtService.generateToken(principal));
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already taken");
        }
        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        RoleName role = request.getRole() != null ? request.getRole() : RoleName.GUEST;
        if (role == RoleName.ADMIN && appUserRepository.count() > 0) {
            throw new BadRequestException("Admin accounts can only be created by an existing administrator");
        }

        AppUser user = AppUser.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .build();

        AppUser saved = appUserRepository.save(user);
        settingsService.createDefaultSettings(saved);
        UserPrincipal principal = new UserPrincipal(saved);
        return buildAuthResponse(principal, jwtService.generateToken(principal));
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile() {
        UserPrincipal principal = SecurityUtils.requireCurrentUser();
        AppUser user = appUserRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .studentsManaged(studentRepository.count())
                .build();
    }

    private AuthResponse buildAuthResponse(UserPrincipal principal, String token) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(principal.getId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .role(principal.getRole())
                .build();
    }
}
