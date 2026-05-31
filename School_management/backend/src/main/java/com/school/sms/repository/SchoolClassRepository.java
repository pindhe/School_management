package com.school.sms.repository;

import com.school.sms.domain.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByAcademicYear_Id(Long academicYearId);

    boolean existsByNameAndAcademicYear_Id(String name, Long academicYearId);
}
