package com.school.registration.service;

import com.school.registration.domain.entity.AppUser;
import com.school.registration.domain.enums.RoleName;
import com.school.registration.dto.auth.AuthResponse;
import com.school.registration.dto.auth.LoginRequest;
import com.school.registration.dto.auth.RegisterRequest;
import com.school.registration.exception.BadRequestException;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.security.JwtService;
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
        UserPrincipal principal = new UserPrincipal(saved);
        return buildAuthResponse(principal, jwtService.generateToken(principal));
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
