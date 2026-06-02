package com.school.registration.config;

import com.school.registration.domain.entity.AppUser;
import com.school.registration.domain.entity.Student;
import com.school.registration.domain.enums.RoleName;
import com.school.registration.repository.AppUserRepository;
import com.school.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (appUserRepository.count() == 0) {
            appUserRepository.save(AppUser.builder()
                    .username("admin")
                    .email("admin@school.local")
                    .password(passwordEncoder.encode("admin123"))
                    .role(RoleName.ADMIN)
                    .enabled(true)
                    .build());

            appUserRepository.save(AppUser.builder()
                    .username("guest")
                    .email("guest@school.local")
                    .password(passwordEncoder.encode("guest123"))
                    .role(RoleName.GUEST)
                    .enabled(true)
                    .build());
        }

        if (studentRepository.count() == 0) {
            studentRepository.save(Student.builder()
                    .name("Amina Hassan")
                    .address("12 Education Street, Cairo")
                    .phone("+20-100-555-0101")
                    .email("amina.hassan@student.local")
                    .build());

            studentRepository.save(Student.builder()
                    .name("Omar Khalil")
                    .address("45 Campus Road, Alexandria")
                    .phone("+20-100-555-0102")
                    .email("omar.khalil@student.local")
                    .build());
        }
    }
}
