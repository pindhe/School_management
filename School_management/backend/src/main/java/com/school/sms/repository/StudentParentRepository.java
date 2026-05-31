package com.school.sms.repository;

import com.school.sms.domain.entity.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentParentRepository extends JpaRepository<StudentParent, Long> {
    List<StudentParent> findByParent_Id(Long parentId);

    List<StudentParent> findByStudent_Id(Long studentId);

    boolean existsByStudent_IdAndParent_Id(Long studentId, Long parentId);
}
