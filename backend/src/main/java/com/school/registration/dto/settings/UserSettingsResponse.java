package com.school.registration.dto.settings;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserSettingsResponse {

    private boolean compactTables;
    private boolean emailNotifications;
    private Instant updatedAt;
}
