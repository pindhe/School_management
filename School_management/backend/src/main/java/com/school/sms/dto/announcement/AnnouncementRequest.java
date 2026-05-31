package com.school.sms.dto.announcement;

import com.school.sms.domain.enums.TargetRole;
import jakarta.validation.constraints.NotBlank;

public record AnnouncementRequest(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Content is required") String content,
        TargetRole targetRole,
        Long targetClassId
) {
}
