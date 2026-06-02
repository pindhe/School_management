package com.school.registration.dto.report;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportSummaryResponse {

    private long totalStudents;
    private long studentsWithPhoto;
    private int photoCoveragePercent;
    private long totalUsers;
    private long adminUsers;
    private long guestUsers;
    private List<DomainCountResponse> studentsByEmailDomain;
}
