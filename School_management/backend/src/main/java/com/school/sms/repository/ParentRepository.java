package com.school.sms.repository;

import com.school.sms.domain.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByUser_Id(Long userId);

    Optional<Parent> findByUser_Username(String username);
}
