package com.school.sms.service;

import com.school.sms.domain.entity.*;
import com.school.sms.dto.academic.*;
import com.school.sms.exception.BadRequestException;
import com.school.sms.exception.ResourceNotFoundException;
import com.school.sms.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AcademicService {

    private final AcademicYearRepository academicYearRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherAssignmentRepository teacherAssignmentRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final TimetableRepository timetableRepository;
    private final DtoMapper mapper;

    public AcademicService(AcademicYearRepository academicYearRepository, SubjectRepository subjectRepository,
                           SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository,
                           TeacherAssignmentRepository teacherAssignmentRepository,
                           StudentRepository studentRepository, EnrollmentRepository enrollmentRepository,
                           TimetableRepository timetableRepository, DtoMapper mapper) {
        this.academicYearRepository = academicYearRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
        this.teacherAssignmentRepository = teacherAssignmentRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.timetableRepository = timetableRepository;
        this.mapper = mapper;
    }

    // ---------- Academic Years ----------

    @Transactional
    public AcademicYearResponse createAcademicYear(AcademicYearRequest req) {
        if (academicYearRepository.existsByName(req.name())) {
            throw new BadRequestException("Academic year '" + req.name() + "' already exists");
        }
        AcademicYear year = AcademicYear.builder()
                .yearStart(req.yearStart())
                .yearEnd(req.yearEnd())
                .name(req.name())
                .active(Boolean.TRUE.equals(req.active()))
                .build();
        return mapper.toAcademicYearResponse(academicYearRepository.save(year));
    }

    public List<AcademicYearResponse> listAcademicYears() {
        return academicYearRepository.findAll().stream().map(mapper::toAcademicYearResponse).toList();
    }

    private AcademicYear requireYear(Long id) {
        return academicYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic year", id));
    }

    // ---------- Subjects ----------

    @Transactional
    public SubjectResponse createSubject(SubjectRequest req) {
        if (subjectRepository.existsByCode(req.code())) {
            throw new BadRequestException("Subject code '" + req.code() + "' already exists");
        }
        if (subjectRepository.existsByName(req.name())) {
            throw new BadRequestException("Subject name '" + req.name() + "' already exists");
        }
        Subject subject = Subject.builder()
                .name(req.name())
                .code(req.code())
                .description(req.description())
                .build();
        return mapper.toSubjectResponse(subjectRepository.save(subject));
    }

    public List<SubjectResponse> listSubjects() {
        return subjectRepository.findAll().stream().map(mapper::toSubjectResponse).toList();
    }

    private Subject requireSubject(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", id));
    }

    // ---------- Classes ----------

    @Transactional
    public ClassResponse createClass(ClassRequest req) {
        if (schoolClassRepository.existsByNameAndAcademicYear_Id(req.name(), req.academicYearId())) {
            throw new BadRequestException("Class '" + req.name() + "' already exists for that academic year");
        }
        AcademicYear year = requireYear(req.academicYearId());
        Teacher homeroom = null;
        if (req.homeroomTeacherId() != null) {
            homeroom = teacherRepository.findById(req.homeroomTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", req.homeroomTeacherId()));
        }
        SchoolClass schoolClass = SchoolClass.builder()
                .name(req.name())
                .academicYear(year)
                .homeroomTeacher(homeroom)
                .subjects(new HashSet<>())
                .build();
        return mapper.toClassResponse(schoolClassRepository.save(schoolClass));
    }

    public List<ClassResponse> listClasses() {
        return schoolClassRepository.findAll().stream().map(mapper::toClassResponse).toList();
    }

    public ClassResponse getClass(Long id) {
        return mapper.toClassResponse(requireClass(id));
    }

    private SchoolClass requireClass(Long id) {
        return schoolClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class", id));
    }

    @Transactional
    public ClassResponse assignSubjectsToClass(Long classId, AssignSubjectsRequest req) {
        SchoolClass schoolClass = requireClass(classId);
        Set<Subject> subjects = new HashSet<>(schoolClass.getSubjects());
        for (Long subjectId : req.subjectIds()) {
            subjects.add(requireSubject(subjectId));
        }
        schoolClass.setSubjects(subjects);
        return mapper.toClassResponse(schoolClassRepository.save(schoolClass));
    }

    // ---------- Teacher Assignments ----------

    @Transactional
    public TeacherAssignmentResponse assignTeacher(TeacherAssignmentRequest req) {
        if (teacherAssignmentRepository.existsByTeacher_IdAndSchoolClass_IdAndSubject_IdAndAcademicYear_Id(
                req.teacherId(), req.classId(), req.subjectId(), req.academicYearId())) {
            throw new BadRequestException("This teacher assignment already exists");
        }
        Teacher teacher = teacherRepository.findById(req.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", req.teacherId()));
        TeacherAssignment assignment = TeacherAssignment.builder()
                .teacher(teacher)
                .schoolClass(requireClass(req.classId()))
                .subject(requireSubject(req.subjectId()))
                .academicYear(requireYear(req.academicYearId()))
                .build();
        return mapper.toTeacherAssignmentResponse(teacherAssignmentRepository.save(assignment));
    }

    public List<TeacherAssignmentResponse> listAssignmentsForTeacher(Long teacherId) {
        return teacherAssignmentRepository.findByTeacher_Id(teacherId).stream()
                .map(mapper::toTeacherAssignmentResponse).toList();
    }

    public List<TeacherAssignmentResponse> listAllAssignments() {
        return teacherAssignmentRepository.findAll().stream()
                .map(mapper::toTeacherAssignmentResponse).toList();
    }

    // ---------- Enrollment ----------

    @Transactional
    public void enrollStudent(EnrollStudentRequest req) {
        if (enrollmentRepository.existsByStudent_IdAndSchoolClass_Id(req.studentId(), req.classId())) {
            throw new BadRequestException("Student is already enrolled in this class");
        }
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", req.studentId()));
        SchoolClass schoolClass = requireClass(req.classId());
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .schoolClass(schoolClass)
                .build();
        enrollmentRepository.save(enrollment);
        student.setCurrentClass(schoolClass);
        studentRepository.save(student);
    }

    // ---------- Timetables ----------

    @Transactional
    public TimetableResponse createTimetableEntry(TimetableRequest req) {
        if (req.endTime().isBefore(req.startTime()) || req.endTime().equals(req.startTime())) {
            throw new BadRequestException("End time must be after start time");
        }
        Teacher teacher = teacherRepository.findById(req.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", req.teacherId()));
        Timetable entry = Timetable.builder()
                .schoolClass(requireClass(req.classId()))
                .subject(requireSubject(req.subjectId()))
                .teacher(teacher)
                .dayOfWeek(req.dayOfWeek())
                .startTime(req.startTime())
                .endTime(req.endTime())
                .roomNumber(req.roomNumber())
                .academicYear(requireYear(req.academicYearId()))
                .build();
        return mapper.toTimetableResponse(timetableRepository.save(entry));
    }

    public List<TimetableResponse> timetableForClass(Long classId) {
        return timetableRepository.findBySchoolClass_IdOrderByDayOfWeekAscStartTimeAsc(classId).stream()
                .map(mapper::toTimetableResponse).toList();
    }

    public List<TimetableResponse> timetableForTeacher(Long teacherId) {
        return timetableRepository.findByTeacher_IdOrderByDayOfWeekAscStartTimeAsc(teacherId).stream()
                .map(mapper::toTimetableResponse).toList();
    }
}
