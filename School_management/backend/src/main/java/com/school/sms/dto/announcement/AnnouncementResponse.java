package com.school.sms.dto.announcement;

import com.school.sms.domain.enums.TargetRole;

import java.time.LocalDateTime;

public record AnnouncementResponse(
        Long id,
        String title,
        String content,
        String publishedByName,
        LocalDateTime publishDate,
        TargetRole targetRole,
        Long targetClassId,
        String targetClassName,
        boolean active
) {
}
