package com.school.sms.dto.academic;

import java.util.List;

public record ClassResponse(
        Long id,
        String name,
        Long academicYearId,
        String academicYearName,
        Long homeroomTeacherId,
        String homeroomTeacherName,
        List<SubjectResponse> subjects
) {
}
