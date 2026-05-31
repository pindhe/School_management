package com.school.sms.service;

import com.school.sms.domain.entity.Grade;
import com.school.sms.domain.entity.Student;
import com.school.sms.domain.entity.Teacher;
import com.school.sms.dto.grade.GradeRequest;
import com.school.sms.dto.grade.GradeResponse;
import com.school.sms.exception.ResourceNotFoundException;
import com.school.sms.repository.*;
import com.school.sms.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final AcademicYearRepository academicYearRepository;
    private final TeacherRepository teacherRepository;
    private final DtoMapper mapper;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository,
                        SubjectRepository subjectRepository, AcademicYearRepository academicYearRepository,
                        TeacherRepository teacherRepository, DtoMapper mapper) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.academicYearRepository = academicYearRepository;
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }

    @Transactional
    public GradeResponse recordGrade(GradeRequest req) {
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", req.studentId()));
        Teacher teacher = teacherRepository.findByUser_Id(SecurityUtils.getCurrentUserId()).orElse(null);
        Grade grade = Grade.builder()
                .student(student)
                .subject(subjectRepository.findById(req.subjectId())
                        .orElseThrow(() -> new ResourceNotFoundException("Subject", req.subjectId())))
                .academicYear(academicYearRepository.findById(req.academicYearId())
                        .orElseThrow(() -> new ResourceNotFoundException("Academic year", req.academicYearId())))
                .gradeType(req.gradeType())
                .score(req.score())
                .maxScore(req.maxScore())
                .gradedByTeacher(teacher)
                .remarks(req.remarks())
                .build();
        return mapper.toGradeResponse(gradeRepository.save(grade));
    }

    @Transactional
    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade", id);
        }
        gradeRepository.deleteById(id);
    }

    public List<GradeResponse> getStudentGrades(Long studentId) {
        return gradeRepository.findByStudent_Id(studentId).stream().map(mapper::toGradeResponse).toList();
    }

    public List<GradeResponse> getStudentGradesByYear(Long studentId, Long academicYearId) {
        return gradeRepository.findByStudent_IdAndAcademicYear_Id(studentId, academicYearId).stream()
                .map(mapper::toGradeResponse).toList();
    }

    public List<GradeResponse> getMyGrades() {
        Student student = studentRepository.findByUser_Id(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user is not a student"));
        return getStudentGrades(student.getId());
    }
}
