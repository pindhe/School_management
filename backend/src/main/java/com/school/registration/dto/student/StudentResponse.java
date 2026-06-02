package com.school.registration.dto.student;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class StudentResponse {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String photoUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
