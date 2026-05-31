package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotNull;

public record EnrollStudentRequest(
        @NotNull(message = "Student is required") Long studentId,
        @NotNull(message = "Class is required") Long classId
) {
}
