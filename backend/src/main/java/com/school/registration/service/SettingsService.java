package com.school.registration.service;

import com.school.registration.domain.entity.AppUser;
import com.school.registration.domain.entity.UserSettings;
import com.school.registration.dto.settings.UserSettingsRequest;
import com.school.registration.dto.settings.UserSettingsResponse;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.UserSettingsRepository;
import com.school.registration.security.SecurityUtils;
import com.school.registration.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public UserSettingsResponse getMySettings() {
        return toResponse(requireSettings(SecurityUtils.requireCurrentUser()));
    }

    @Transactional
    public UserSettingsResponse saveMySettings(UserSettingsRequest request) {
        UserSettings settings = requireSettings(SecurityUtils.requireCurrentUser());
        settings.setCompactTables(request.isCompactTables());
        settings.setEmailNotifications(request.isEmailNotifications());
        return toResponse(userSettingsRepository.save(settings));
    }

    @Transactional
    public UserSettings createDefaultSettings(AppUser user) {
        UserSettings settings = UserSettings.builder()
                .user(user)
                .compactTables(false)
                .emailNotifications(true)
                .build();
        return userSettingsRepository.save(settings);
    }

    private UserSettings requireSettings(UserPrincipal principal) {
        return userSettingsRepository.findByUserId(principal.getId())
                .orElseGet(() -> {
                    AppUser user = appUserRepository.findById(principal.getId()).orElseThrow();
                    return createDefaultSettings(user);
                });
    }

    private UserSettingsResponse toResponse(UserSettings settings) {
        return UserSettingsResponse.builder()
                .compactTables(settings.isCompactTables())
                .emailNotifications(settings.isEmailNotifications())
                .updatedAt(settings.getUpdatedAt())
                .build();
    }
}
