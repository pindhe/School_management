package com.school.sms.dto.grade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record GradeRequest(
        @NotNull(message = "Student is required") Long studentId,
        @NotNull(message = "Subject is required") Long subjectId,
        @NotNull(message = "Academic year is required") Long academicYearId,
        @NotBlank(message = "Grade type is required") String gradeType,
        @NotNull(message = "Score is required") @PositiveOrZero(message = "Score must be zero or positive") BigDecimal score,
        @NotNull(message = "Max score is required") @Positive(message = "Max score must be positive") BigDecimal maxScore,
        String remarks
) {
}
