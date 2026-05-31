package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TimetableRequest(
        @NotNull(message = "Class is required") Long classId,
        @NotNull(message = "Subject is required") Long subjectId,
        @NotNull(message = "Teacher is required") Long teacherId,
        @NotNull(message = "Day of week is required") DayOfWeek dayOfWeek,
        @NotNull(message = "Start time is required") LocalTime startTime,
        @NotNull(message = "End time is required") LocalTime endTime,
        String roomNumber,
        @NotNull(message = "Academic year is required") Long academicYearId
) {
}
