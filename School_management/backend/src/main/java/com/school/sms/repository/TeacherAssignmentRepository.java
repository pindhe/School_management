package com.school.sms.repository;

import com.school.sms.domain.entity.TeacherAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherAssignmentRepository extends JpaRepository<TeacherAssignment, Long> {
    List<TeacherAssignment> findByTeacher_Id(Long teacherId);

    List<TeacherAssignment> findBySchoolClass_Id(Long classId);

    boolean existsByTeacher_IdAndSchoolClass_IdAndSubject_IdAndAcademicYear_Id(
            Long teacherId, Long classId, Long subjectId, Long academicYearId);
}
