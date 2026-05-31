package com.school.sms.dto.attendance;

import com.school.sms.domain.enums.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceResponse(
        Long id,
        Long studentId,
        String studentName,
        LocalDate date,
        AttendanceStatus status,
        String remarks,
        String recordedByTeacherName
) {
}
