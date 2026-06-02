package com.school.registration.service;

import com.school.registration.domain.enums.RoleName;
import com.school.registration.dto.system.SystemInfoResponse;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Value("${spring.application.name:student-registration}")
    private String applicationName;

    @Transactional(readOnly = true)
    public SystemInfoResponse getInfo() {
        studentRepository.count();
        return SystemInfoResponse.builder()
                .applicationName(applicationName)
                .version("1.0.0")
                .databaseStatus("CONNECTED")
                .totalStudents(studentRepository.count())
                .totalUsers(appUserRepository.count())
                .adminUsers(appUserRepository.countByRole(RoleName.ADMIN))
                .guestUsers(appUserRepository.countByRole(RoleName.GUEST))
                .build();
    }
}
