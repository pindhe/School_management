package com.school.sms.service;

import com.school.sms.domain.entity.*;
import com.school.sms.domain.enums.RoleName;
import com.school.sms.dto.user.*;
import com.school.sms.exception.BadRequestException;
import com.school.sms.exception.ResourceNotFoundException;
import com.school.sms.repository.*;
import com.school.sms.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final StudentParentRepository studentParentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper mapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       StudentRepository studentRepository, TeacherRepository teacherRepository,
                       ParentRepository parentRepository, StudentParentRepository studentParentRepository,
                       SchoolClassRepository schoolClassRepository, PasswordEncoder passwordEncoder,
                       DtoMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.parentRepository = parentRepository;
        this.studentParentRepository = studentParentRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    private Role requireRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role " + name + " is not configured"));
    }

    private void validateUniqueCredentials(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username '" + username + "' is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email '" + email + "' is already registered");
        }
    }

    private String resolvePassword(String raw) {
        String value = (raw == null || raw.isBlank()) ? "Welcome@123" : raw;
        return passwordEncoder.encode(value);
    }

    // ---------- Teachers ----------

    @Transactional
    public TeacherResponse createTeacher(CreateTeacherRequest req) {
        validateUniqueCredentials(req.username(), req.email());
        if (teacherRepository.existsByEmployeeId(req.employeeId())) {
            throw new BadRequestException("Employee ID '" + req.employeeId() + "' already exists");
        }
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(resolvePassword(req.password()))
                .firstName(req.firstName())
                .lastName(req.lastName())
                .dateOfBirth(req.dateOfBirth())
                .gender(req.gender())
                .phoneNumber(req.phoneNumber())
                .address(req.address())
                .role(requireRole(RoleName.TEACHER))
                .active(true)
                .build();
        user = userRepository.save(user);

        Teacher teacher = Teacher.builder()
                .user(user)
                .employeeId(req.employeeId())
                .department(req.department())
                .hireDate(req.hireDate())
                .build();
        return mapper.toTeacherResponse(teacherRepository.save(teacher));
    }

    public List<TeacherResponse> listTeachers() {
        return teacherRepository.findAll().stream().map(mapper::toTeacherResponse).toList();
    }

    public TeacherResponse getTeacher(Long id) {
        return mapper.toTeacherResponse(teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id)));
    }

    // ---------- Students ----------

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest req) {
        validateUniqueCredentials(req.username(), req.email());
        if (studentRepository.existsByAdmissionNumber(req.admissionNumber())) {
            throw new BadRequestException("Admission number '" + req.admissionNumber() + "' already exists");
        }
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(resolvePassword(req.password()))
                .firstName(req.firstName())
                .lastName(req.lastName())
                .dateOfBirth(req.dateOfBirth())
                .gender(req.gender())
                .phoneNumber(req.phoneNumber())
                .address(req.address())
                .role(requireRole(RoleName.STUDENT))
                .active(true)
                .build();
        user = userRepository.save(user);

        SchoolClass currentClass = null;
        if (req.currentClassId() != null) {
            currentClass = schoolClassRepository.findById(req.currentClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", req.currentClassId()));
        }

        Student student = Student.builder()
                .user(user)
                .admissionNumber(req.admissionNumber())
                .currentClass(currentClass)
                .dateOfAdmission(req.dateOfAdmission())
                .build();
        return mapper.toStudentResponse(studentRepository.save(student));
    }

    public List<StudentResponse> listStudents() {
        return studentRepository.findAll().stream().map(mapper::toStudentResponse).toList();
    }

    public StudentResponse getStudent(Long id) {
        return mapper.toStudentResponse(studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id)));
    }

    public List<StudentResponse> listStudentsByClass(Long classId) {
        return studentRepository.findByCurrentClass_Id(classId).stream()
                .map(mapper::toStudentResponse).toList();
    }

    // ---------- Parents ----------

    @Transactional
    public ParentResponse createParent(CreateParentRequest req) {
        validateUniqueCredentials(req.username(), req.email());
        User user = User.builder()
                .username(req.username())
                .email(req.email())
                .passwordHash(resolvePassword(req.password()))
                .firstName(req.firstName())
                .lastName(req.lastName())
                .dateOfBirth(req.dateOfBirth())
                .gender(req.gender())
                .phoneNumber(req.phoneNumber())
                .address(req.address())
                .role(requireRole(RoleName.PARENT))
                .active(true)
                .build();
        user = userRepository.save(user);

        Parent parent = Parent.builder().user(user).build();
        parent = parentRepository.save(parent);
        return mapper.toParentResponse(parent, List.of());
    }

    public List<ParentResponse> listParents() {
        return parentRepository.findAll().stream()
                .map(p -> mapper.toParentResponse(p, childrenOf(p.getId())))
                .toList();
    }

    public ParentResponse getParent(Long id) {
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parent", id));
        return mapper.toParentResponse(parent, childrenOf(id));
    }

    private List<StudentResponse> childrenOf(Long parentId) {
        return studentParentRepository.findByParent_Id(parentId).stream()
                .map(sp -> mapper.toStudentResponse(sp.getStudent()))
                .toList();
    }

    @Transactional
    public void linkParentToStudent(LinkParentRequest req) {
        if (studentParentRepository.existsByStudent_IdAndParent_Id(req.studentId(), req.parentId())) {
            throw new BadRequestException("This parent is already linked to the student");
        }
        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", req.studentId()));
        Parent parent = parentRepository.findById(req.parentId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent", req.parentId()));
        StudentParent link = StudentParent.builder()
                .student(student)
                .parent(parent)
                .relationship(req.relationship())
                .build();
        studentParentRepository.save(link);
    }

    // ---------- Generic user operations ----------

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(mapper::toUserResponse).toList();
    }

    @Transactional
    public UserResponse setActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        user.setActive(active);
        return mapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        if (req.email() != null && !req.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.email())) {
                throw new BadRequestException("Email '" + req.email() + "' is already registered");
            }
            user.setEmail(req.email());
        }
        if (req.firstName() != null) user.setFirstName(req.firstName());
        if (req.lastName() != null) user.setLastName(req.lastName());
        if (req.dateOfBirth() != null) user.setDateOfBirth(req.dateOfBirth());
        if (req.address() != null) user.setAddress(req.address());
        if (req.phoneNumber() != null) user.setPhoneNumber(req.phoneNumber());
        if (req.gender() != null) user.setGender(req.gender());
        return mapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateOwnProfile(UpdateProfileRequest req) {
        return updateProfile(SecurityUtils.getCurrentUserId(), req);
    }
}
