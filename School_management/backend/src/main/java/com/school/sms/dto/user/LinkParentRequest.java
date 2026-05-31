package com.school.sms.dto.user;

import jakarta.validation.constraints.NotNull;

public record LinkParentRequest(
        @NotNull(message = "Student ID is required") Long studentId,
        @NotNull(message = "Parent ID is required") Long parentId,
        String relationship
) {
}
