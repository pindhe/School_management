package com.school.registration.service;

import com.school.registration.domain.entity.AppUser;
import com.school.registration.dto.user.UserSummaryResponse;
import com.school.registration.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;

    public List<UserSummaryResponse> findAll() {
        return appUserRepository.findAll().stream()
                .sorted(Comparator.comparing(AppUser::getUsername))
                .map(this::toResponse)
                .toList();
    }

    private UserSummaryResponse toResponse(AppUser user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
