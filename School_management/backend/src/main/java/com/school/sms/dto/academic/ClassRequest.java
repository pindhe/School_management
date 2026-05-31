package com.school.sms.dto.academic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassRequest(
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Academic year is required") Long academicYearId,
        Long homeroomTeacherId
) {
}
