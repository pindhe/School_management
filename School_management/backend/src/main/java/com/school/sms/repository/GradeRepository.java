package com.school.sms.repository;

import com.school.sms.domain.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudent_Id(Long studentId);

    List<Grade> findByStudent_IdAndAcademicYear_Id(Long studentId, Long academicYearId);

    List<Grade> findBySubject_Id(Long subjectId);
}
