package com.school.sms.repository;

import com.school.sms.domain.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    Optional<AcademicYear> findByName(String name);

    Optional<AcademicYear> findFirstByActiveTrue();

    boolean existsByName(String name);
}
