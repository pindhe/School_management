package com.school.sms.repository;

import com.school.sms.domain.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudent_IdAndDate(Long studentId, LocalDate date);

    List<Attendance> findByStudent_IdOrderByDateDesc(Long studentId);

    List<Attendance> findByStudent_IdAndDateBetweenOrderByDate(Long studentId, LocalDate start, LocalDate end);

    List<Attendance> findByStudent_CurrentClass_IdAndDate(Long classId, LocalDate date);
}
