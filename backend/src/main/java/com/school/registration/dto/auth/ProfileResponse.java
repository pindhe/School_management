package com.school.registration.dto.auth;

import com.school.registration.domain.enums.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProfileResponse {

    private Long id;
    private String username;
    private String email;
    private RoleName role;
    private boolean enabled;
    private Instant createdAt;
    private long studentsManaged;
}
