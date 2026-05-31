package com.school.sms.repository;

import com.school.sms.domain.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findBySchoolClass_Id(Long classId);

    List<Enrollment> findByStudent_Id(Long studentId);

    boolean existsByStudent_IdAndSchoolClass_Id(Long studentId, Long classId);
}
