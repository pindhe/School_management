package com.school.sms.service;

import com.school.sms.domain.entity.*;
import com.school.sms.dto.academic.*;
import com.school.sms.dto.announcement.AnnouncementResponse;
import com.school.sms.dto.attendance.AttendanceResponse;
import com.school.sms.dto.grade.GradeResponse;
import com.school.sms.dto.user.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class DtoMapper {

    public UserResponse toUserResponse(User u) {
        return new UserResponse(
                u.getId(), u.getUsername(), u.getEmail(), u.getFirstName(), u.getLastName(),
                u.getFullName(), u.getDateOfBirth(), u.getAddress(), u.getPhoneNumber(),
                u.getGender(), u.getRole().getName(), u.isActive());
    }

    public TeacherResponse toTeacherResponse(Teacher t) {
        return new TeacherResponse(t.getId(), toUserResponse(t.getUser()),
                t.getEmployeeId(), t.getDepartment(), t.getHireDate());
    }

    public StudentResponse toStudentResponse(Student s) {
        Long classId = s.getCurrentClass() != null ? s.getCurrentClass().getId() : null;
        String className = s.getCurrentClass() != null ? s.getCurrentClass().getName() : null;
        return new StudentResponse(s.getId(), toUserResponse(s.getUser()),
                s.getAdmissionNumber(), classId, className, s.getDateOfAdmission());
    }

    public ParentResponse toParentResponse(Parent p, List<StudentResponse> children) {
        return new ParentResponse(p.getId(), toUserResponse(p.getUser()), children);
    }

    public AcademicYearResponse toAcademicYearResponse(AcademicYear a) {
        return new AcademicYearResponse(a.getId(), a.getYearStart(), a.getYearEnd(), a.getName(), a.isActive());
    }

    public SubjectResponse toSubjectResponse(Subject s) {
        return new SubjectResponse(s.getId(), s.getName(), s.getCode(), s.getDescription());
    }

    public ClassResponse toClassResponse(SchoolClass c) {
        Long teacherId = c.getHomeroomTeacher() != null ? c.getHomeroomTeacher().getId() : null;
        String teacherName = c.getHomeroomTeacher() != null
                ? c.getHomeroomTeacher().getUser().getFullName() : null;
        List<SubjectResponse> subjects = c.getSubjects().stream().map(this::toSubjectResponse).toList();
        return new ClassResponse(c.getId(), c.getName(), c.getAcademicYear().getId(),
                c.getAcademicYear().getName(), teacherId, teacherName, subjects);
    }

    public TeacherAssignmentResponse toTeacherAssignmentResponse(TeacherAssignment a) {
        return new TeacherAssignmentResponse(
                a.getId(), a.getTeacher().getId(), a.getTeacher().getUser().getFullName(),
                a.getSchoolClass().getId(), a.getSchoolClass().getName(),
                a.getSubject().getId(), a.getSubject().getName(),
                a.getAcademicYear().getId(), a.getAcademicYear().getName());
    }

    public TimetableResponse toTimetableResponse(Timetable t) {
        return new TimetableResponse(
                t.getId(), t.getSchoolClass().getId(), t.getSchoolClass().getName(),
                t.getSubject().getId(), t.getSubject().getName(),
                t.getTeacher().getId(), t.getTeacher().getUser().getFullName(),
                t.getDayOfWeek(), t.getStartTime(), t.getEndTime(), t.getRoomNumber(),
                t.getAcademicYear().getId());
    }

    public AttendanceResponse toAttendanceResponse(Attendance a) {
        String teacherName = a.getRecordedByTeacher() != null
                ? a.getRecordedByTeacher().getUser().getFullName() : null;
        return new AttendanceResponse(a.getId(), a.getStudent().getId(),
                a.getStudent().getUser().getFullName(), a.getDate(), a.getStatus(),
                a.getRemarks(), teacherName);
    }

    public GradeResponse toGradeResponse(Grade g) {
        double percentage = 0.0;
        if (g.getMaxScore() != null && g.getMaxScore().compareTo(BigDecimal.ZERO) > 0) {
            percentage = g.getScore()
                    .divide(g.getMaxScore(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        String teacherName = g.getGradedByTeacher() != null
                ? g.getGradedByTeacher().getUser().getFullName() : null;
        return new GradeResponse(g.getId(), g.getStudent().getId(),
                g.getStudent().getUser().getFullName(), g.getSubject().getId(), g.getSubject().getName(),
                g.getAcademicYear().getId(), g.getAcademicYear().getName(), g.getGradeType(),
                g.getScore(), g.getMaxScore(), percentage, letterGrade(percentage),
                teacherName, g.getGradeDate(), g.getRemarks());
    }

    public AnnouncementResponse toAnnouncementResponse(Announcement a) {
        Long classId = a.getTargetClass() != null ? a.getTargetClass().getId() : null;
        String className = a.getTargetClass() != null ? a.getTargetClass().getName() : null;
        return new AnnouncementResponse(a.getId(), a.getTitle(), a.getContent(),
                a.getPublishedBy().getFullName(), a.getPublishDate(), a.getTargetRole(),
                classId, className, a.isActive());
    }

    public String letterGrade(double percentage) {
        if (percentage >= 90) return "A";
        if (percentage >= 80) return "B";
        if (percentage >= 70) return "C";
        if (percentage >= 60) return "D";
        if (percentage >= 50) return "E";
        return "F";
    }
}
