package com.school.sms.dto.user;

import com.school.sms.domain.enums.Gender;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UpdateProfileRequest(
        @Email(message = "Email must be valid") String email,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        String phoneNumber,
        Gender gender
) {
}
