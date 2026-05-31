package com.school.sms.dto.grade;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GradeResponse(
        Long id,
        Long studentId,
        String studentName,
        Long subjectId,
        String subjectName,
        Long academicYearId,
        String academicYearName,
        String gradeType,
        BigDecimal score,
        BigDecimal maxScore,
        Double percentage,
        String letterGrade,
        String gradedByTeacherName,
        LocalDateTime gradeDate,
        String remarks
) {
}
