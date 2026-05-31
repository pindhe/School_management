package com.school.sms.dto.academic;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TimetableResponse(
        Long id,
        Long classId,
        String className,
        Long subjectId,
        String subjectName,
        Long teacherId,
        String teacherName,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        String roomNumber,
        Long academicYearId
) {
}
