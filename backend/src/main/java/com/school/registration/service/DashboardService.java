package com.school.registration.service;

import com.school.registration.domain.entity.Student;
import com.school.registration.domain.enums.RoleName;
import com.school.registration.dto.dashboard.DashboardStatsResponse;
import com.school.registration.dto.student.StudentResponse;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final StudentService studentService;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        long totalStudents = studentRepository.count();
        long withPhoto = studentRepository.countByPhotoPathIsNotNull();

        List<StudentResponse> recent = studentRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(studentService::toResponse)
                .toList();

        return DashboardStatsResponse.builder()
                .totalStudents(totalStudents)
                .studentsWithPhoto(withPhoto)
                .studentsWithoutPhoto(totalStudents - withPhoto)
                .totalUsers(appUserRepository.count())
                .adminUsers(appUserRepository.countByRole(RoleName.ADMIN))
                .guestUsers(appUserRepository.countByRole(RoleName.GUEST))
                .recentStudents(recent)
                .build();
    }
}
