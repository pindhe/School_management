package com.school.registration.dto.auth;

import com.school.registration.domain.enums.RoleName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private String type;
    private Long userId;
    private String username;
    private String email;
    private RoleName role;
}
