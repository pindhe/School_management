package com.school.sms.dto.attendance;

import com.school.sms.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceEntry(
        @NotNull(message = "Student is required") Long studentId,
        @NotNull(message = "Status is required") AttendanceStatus status,
        String remarks
) {
}
