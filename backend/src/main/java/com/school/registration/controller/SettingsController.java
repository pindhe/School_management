package com.school.registration.controller;

import com.school.registration.dto.settings.UserSettingsRequest;
import com.school.registration.dto.settings.UserSettingsResponse;
import com.school.registration.service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping("/me")
    public UserSettingsResponse getMySettings() {
        return settingsService.getMySettings();
    }

    @PutMapping("/me")
    public UserSettingsResponse saveMySettings(@Valid @RequestBody UserSettingsRequest request) {
        return settingsService.saveMySettings(request);
    }
}
