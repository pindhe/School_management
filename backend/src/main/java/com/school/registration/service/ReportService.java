package com.school.registration.service;

import com.school.registration.domain.entity.Student;
import com.school.registration.domain.enums.RoleName;
import com.school.registration.dto.report.DomainCountResponse;
import com.school.registration.dto.report.ReportSummaryResponse;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public ReportSummaryResponse getSummary() {
        List<Student> students = studentRepository.findAll();
        long total = students.size();
        long withPhoto = students.stream().filter(s -> s.getPhotoPath() != null && !s.getPhotoPath().isBlank()).count();
        int coverage = total == 0 ? 0 : (int) Math.round((withPhoto * 100.0) / total);

        Map<String, Long> domainCounts = new LinkedHashMap<>();
        for (Student student : students) {
            String domain = extractDomain(student.getEmail());
            domainCounts.merge(domain, 1L, Long::sum);
        }

        List<DomainCountResponse> byDomain = domainCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(e -> new DomainCountResponse(e.getKey(), e.getValue()))
                .toList();

        return ReportSummaryResponse.builder()
                .totalStudents(total)
                .studentsWithPhoto(withPhoto)
                .photoCoveragePercent(coverage)
                .totalUsers(appUserRepository.count())
                .adminUsers(appUserRepository.countByRole(RoleName.ADMIN))
                .guestUsers(appUserRepository.countByRole(RoleName.GUEST))
                .studentsByEmailDomain(byDomain)
                .build();
    }

    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            return "unknown";
        }
        return email.substring(email.indexOf('@') + 1).toLowerCase();
    }
}
