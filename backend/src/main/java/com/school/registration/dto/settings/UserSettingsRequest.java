package com.school.registration.dto.settings;

import lombok.Data;

@Data
public class UserSettingsRequest {

    private boolean compactTables;
    private boolean emailNotifications;
}
