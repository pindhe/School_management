package com.school.sms.dto.academic;

public record AcademicYearResponse(
        Long id,
        Integer yearStart,
        Integer yearEnd,
        String name,
        boolean active
) {
}
