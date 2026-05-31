package com.school.sms.dto.academic;

public record TeacherAssignmentResponse(
        Long id,
        Long teacherId,
        String teacherName,
        Long classId,
        String className,
        Long subjectId,
        String subjectName,
        Long academicYearId,
        String academicYearName
) {
}
