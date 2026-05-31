package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotBlank;

public record SubjectRequest(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Code is required") String code,
        String description
) {
}
