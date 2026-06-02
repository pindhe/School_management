package com.school.registration.repository;

import com.school.registration.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("""
            SELECT s FROM Student s
            WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(s.email) LIKE LOWER(CONCAT('%', :q, '%'))
               OR s.phone LIKE CONCAT('%', :q, '%')
               OR LOWER(s.address) LIKE LOWER(CONCAT('%', :q, '%'))
            ORDER BY s.name ASC
            """)
    List<Student> search(@Param("q") String query);
}
