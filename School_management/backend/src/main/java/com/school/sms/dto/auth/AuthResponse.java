package com.school.sms.dto.auth;

import com.school.sms.dto.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
}
