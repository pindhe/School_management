package com.school.sms.dto.user;

import java.util.List;

public record ParentResponse(
        Long id,
        UserResponse user,
        List<StudentResponse> children
) {
}
