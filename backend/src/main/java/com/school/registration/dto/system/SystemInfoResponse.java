package com.school.registration.dto.system;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemInfoResponse {

    private String applicationName;
    private String version;
    private String databaseStatus;
    private long totalStudents;
    private long totalUsers;
    private long adminUsers;
    private long guestUsers;
}
