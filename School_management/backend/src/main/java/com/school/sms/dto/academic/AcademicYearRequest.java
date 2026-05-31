package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AcademicYearRequest(
        @NotNull(message = "Start year is required") Integer yearStart,
        @NotNull(message = "End year is required") Integer yearEnd,
        @NotBlank(message = "Name is required") String name,
        Boolean active
) {
}
