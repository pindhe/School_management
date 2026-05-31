package com.school.sms.dto.user;

import java.time.LocalDate;

public record TeacherResponse(
        Long id,
        UserResponse user,
        String employeeId,
        String department,
        LocalDate hireDate
) {
}
