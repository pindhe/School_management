package com.school.sms.service;

import com.school.sms.domain.entity.Attendance;
import com.school.sms.domain.entity.Student;
import com.school.sms.domain.entity.Teacher;
import com.school.sms.dto.attendance.AttendanceEntry;
import com.school.sms.dto.attendance.AttendanceResponse;
import com.school.sms.dto.attendance.MarkAttendanceRequest;
import com.school.sms.exception.ResourceNotFoundException;
import com.school.sms.repository.AttendanceRepository;
import com.school.sms.repository.StudentRepository;
import com.school.sms.repository.TeacherRepository;
import com.school.sms.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DtoMapper mapper;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository,
                             TeacherRepository teacherRepository, DtoMapper mapper) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }

    private Teacher currentTeacher() {
        return teacherRepository.findByUser_Id(SecurityUtils.getCurrentUserId()).orElse(null);
    }

    @Transactional
    public List<AttendanceResponse> markAttendance(MarkAttendanceRequest request) {
        Teacher teacher = currentTeacher();
        for (AttendanceEntry entry : request.entries()) {
            Student student = studentRepository.findById(entry.studentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", entry.studentId()));
            Attendance attendance = attendanceRepository
                    .findByStudent_IdAndDate(entry.studentId(), request.date())
                    .orElseGet(() -> Attendance.builder()
                            .student(student)
                            .date(request.date())
                            .build());
            attendance.setStatus(entry.status());
            attendance.setRemarks(entry.remarks());
            attendance.setRecordedByTeacher(teacher);
            attendanceRepository.save(attendance);
        }
        return attendanceRepository.findByStudent_CurrentClass_IdAndDate(
                        resolveClassId(request), request.date()).stream()
                .map(mapper::toAttendanceResponse).toList();
    }

    private Long resolveClassId(MarkAttendanceRequest request) {
        Long firstStudentId = request.entries().get(0).studentId();
        Student student = studentRepository.findById(firstStudentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", firstStudentId));
        return student.getCurrentClass() != null ? student.getCurrentClass().getId() : -1L;
    }

    public List<AttendanceResponse> getClassAttendance(Long classId, LocalDate date) {
        return attendanceRepository.findByStudent_CurrentClass_IdAndDate(classId, date).stream()
                .map(mapper::toAttendanceResponse).toList();
    }

    public List<AttendanceResponse> getStudentAttendance(Long studentId) {
        return attendanceRepository.findByStudent_IdOrderByDateDesc(studentId).stream()
                .map(mapper::toAttendanceResponse).toList();
    }

    public List<AttendanceResponse> getStudentAttendanceRange(Long studentId, LocalDate start, LocalDate end) {
        return attendanceRepository.findByStudent_IdAndDateBetweenOrderByDate(studentId, start, end).stream()
                .map(mapper::toAttendanceResponse).toList();
    }

    public List<AttendanceResponse> getMyAttendance() {
        Student student = studentRepository.findByUser_Id(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user is not a student"));
        return getStudentAttendance(student.getId());
    }
}
