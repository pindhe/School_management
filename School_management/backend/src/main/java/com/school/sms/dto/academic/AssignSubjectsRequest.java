package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AssignSubjectsRequest(
        @NotEmpty(message = "At least one subject is required") List<Long> subjectIds
) {
}
