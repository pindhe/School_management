package com.school.registration.dto.dashboard;

import com.school.registration.dto.student.StudentResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardStatsResponse {

    private long totalStudents;
    private long studentsWithPhoto;
    private long studentsWithoutPhoto;
    private long totalUsers;
    private long adminUsers;
    private long guestUsers;
    private List<StudentResponse> recentStudents;
}
