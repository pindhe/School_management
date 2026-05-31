package com.school.sms.config;

import com.school.sms.domain.entity.*;
import com.school.sms.domain.enums.AttendanceStatus;
import com.school.sms.domain.enums.Gender;
import com.school.sms.domain.enums.RoleName;
import com.school.sms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final StudentParentRepository studentParentRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;
    @Value("${app.seed.admin-username:admin}")
    private String adminUsername;
    @Value("${app.seed.admin-password:Admin@123}")
    private String adminPassword;
    @Value("${app.seed.admin-email:admin@school.local}")
    private String adminEmail;

    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository,
                      TeacherRepository teacherRepository, StudentRepository studentRepository,
                      ParentRepository parentRepository, StudentParentRepository studentParentRepository,
                      AcademicYearRepository academicYearRepository, SubjectRepository subjectRepository,
                      SchoolClassRepository schoolClassRepository, EnrollmentRepository enrollmentRepository,
                      AttendanceRepository attendanceRepository, GradeRepository gradeRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.studentParentRepository = studentParentRepository;
        this.academicYearRepository = academicYearRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.attendanceRepository = attendanceRepository;
        this.gradeRepository = gradeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }
        seedRoles();
        seedAdmin();
        seedDemoData();
    }

    private void seedRoles() {
        for (RoleName name : RoleName.values()) {
            roleRepository.findByName(name).orElseGet(() ->
                    roleRepository.save(Role.builder()
                            .name(name)
                            .description(name.name() + " role")
                            .build()));
        }
    }

    private Role role(RoleName name) {
        return roleRepository.findByName(name).orElseThrow();
    }

    private void seedAdmin() {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        User admin = User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(adminPassword))
                .firstName("System")
                .lastName("Administrator")
                .gender(Gender.OTHER)
                .role(role(RoleName.ADMIN))
                .active(true)
                .build();
        userRepository.save(admin);
        log.info("Seeded default admin account: username='{}'", adminUsername);
    }

    private void seedDemoData() {
        if (academicYearRepository.count() > 0) {
            return;
        }

        AcademicYear year = academicYearRepository.save(AcademicYear.builder()
                .yearStart(2025).yearEnd(2026).name("2025-2026").active(true).build());

        Subject math = subjectRepository.save(Subject.builder()
                .name("Mathematics").code("MATH").description("Core mathematics").build());
        Subject english = subjectRepository.save(Subject.builder()
                .name("English").code("ENG").description("English language").build());
        Subject science = subjectRepository.save(Subject.builder()
                .name("Science").code("SCI").description("General science").build());

        User teacherUser = userRepository.save(User.builder()
                .username("teacher").email("teacher@school.local")
                .passwordHash(passwordEncoder.encode("Teacher@123"))
                .firstName("Jane").lastName("Smith").gender(Gender.FEMALE)
                .role(role(RoleName.TEACHER)).active(true).build());
        Teacher teacher = teacherRepository.save(Teacher.builder()
                .user(teacherUser).employeeId("EMP-001").department("Mathematics")
                .hireDate(LocalDate.of(2020, 8, 1)).build());

        Set<Subject> classSubjects = new HashSet<>(Set.of(math, english, science));
        SchoolClass grade5 = schoolClassRepository.save(SchoolClass.builder()
                .name("Grade 5A").academicYear(year).homeroomTeacher(teacher)
                .subjects(classSubjects).build());

        User studentUser = userRepository.save(User.builder()
                .username("student").email("student@school.local")
                .passwordHash(passwordEncoder.encode("Student@123"))
                .firstName("Tom").lastName("Brown").gender(Gender.MALE)
                .role(role(RoleName.STUDENT)).active(true).build());
        Student student = studentRepository.save(Student.builder()
                .user(studentUser).admissionNumber("ADM-1001").currentClass(grade5)
                .dateOfAdmission(LocalDate.of(2025, 9, 1)).build());

        enrollmentRepository.save(Enrollment.builder().student(student).schoolClass(grade5).build());

        User parentUser = userRepository.save(User.builder()
                .username("parent").email("parent@school.local")
                .passwordHash(passwordEncoder.encode("Parent@123"))
                .firstName("Robert").lastName("Brown").gender(Gender.MALE)
                .role(role(RoleName.PARENT)).active(true).build());
        Parent parent = parentRepository.save(Parent.builder().user(parentUser).build());
        studentParentRepository.save(StudentParent.builder()
                .student(student).parent(parent).relationship("FATHER").build());

        attendanceRepository.save(Attendance.builder()
                .student(student).date(LocalDate.now()).status(AttendanceStatus.PRESENT)
                .recordedByTeacher(teacher).build());

        gradeRepository.save(Grade.builder()
                .student(student).subject(math).academicYear(year)
                .gradeType("Midterm").score(new BigDecimal("82")).maxScore(new BigDecimal("100"))
                .gradedByTeacher(teacher).remarks("Good progress").build());

        log.info("Seeded demo data (academic year, subjects, class, teacher, student, parent).");
    }
}
