package com.school.sms.repository;

import com.school.sms.domain.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByCode(String code);

    boolean existsByName(String name);
}
