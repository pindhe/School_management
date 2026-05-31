package com.school.sms.dto.user;

import com.school.sms.domain.enums.Gender;
import com.school.sms.domain.enums.RoleName;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        String fullName,
        LocalDate dateOfBirth,
        String address,
        String phoneNumber,
        Gender gender,
        RoleName role,
        boolean active
) {
}
