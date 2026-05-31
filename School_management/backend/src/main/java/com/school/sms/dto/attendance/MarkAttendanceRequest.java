package com.school.sms.dto.attendance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MarkAttendanceRequest(
        @NotNull(message = "Date is required") LocalDate date,
        @NotEmpty(message = "At least one attendance entry is required")
        @Valid List<AttendanceEntry> entries
) {
}
