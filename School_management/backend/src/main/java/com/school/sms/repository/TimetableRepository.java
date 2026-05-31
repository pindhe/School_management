package com.school.sms.repository;

import com.school.sms.domain.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findBySchoolClass_IdOrderByDayOfWeekAscStartTimeAsc(Long classId);

    List<Timetable> findByTeacher_IdOrderByDayOfWeekAscStartTimeAsc(Long teacherId);
}
