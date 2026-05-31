package com.school.sms.dto.user;

import com.school.sms.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTeacherRequest(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
        @NotBlank(message = "Username is required") String username,
        @Size(min = 6, message = "Password must be at least 6 characters") String password,
        @NotBlank(message = "Employee ID is required") String employeeId,
        LocalDate dateOfBirth,
        Gender gender,
        String phoneNumber,
        String address,
        String department,
        LocalDate hireDate
) {
}
