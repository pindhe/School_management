package com.school.sms.dto.user;

import java.time.LocalDate;

public record StudentResponse(
        Long id,
        UserResponse user,
        String admissionNumber,
        Long currentClassId,
        String currentClassName,
        LocalDate dateOfAdmission
) {
}
