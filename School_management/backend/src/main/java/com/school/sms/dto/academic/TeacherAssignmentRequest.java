package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotNull;

public record TeacherAssignmentRequest(
        @NotNull(message = "Teacher is required") Long teacherId,
        @NotNull(message = "Class is required") Long classId,
        @NotNull(message = "Subject is required") Long subjectId,
        @NotNull(message = "Academic year is required") Long academicYearId
) {
}
