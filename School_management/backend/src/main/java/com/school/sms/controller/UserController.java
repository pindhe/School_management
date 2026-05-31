package com.school.sms.controller;

import com.school.sms.dto.common.MessageResponse;
import com.school.sms.dto.user.*;
import com.school.sms.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Admin management of teachers, students and parents")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> setActive(@PathVariable Long userId, @RequestParam boolean active) {
        return ResponseEntity.ok(userService.setActive(userId, active));
    }

    @PutMapping("/{userId}/profile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long userId,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }

    // ---------- Teachers ----------

    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherResponse> createTeacher(@Valid @RequestBody CreateTeacherRequest request) {
        return new ResponseEntity<>(userService.createTeacher(request), HttpStatus.CREATED);
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<TeacherResponse>> listTeachers() {
        return ResponseEntity.ok(userService.listTeachers());
    }

    @GetMapping("/teachers/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<TeacherResponse> getTeacher(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getTeacher(id));
    }

    // ---------- Students ----------

    @PostMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        return new ResponseEntity<>(userService.createStudent(request), HttpStatus.CREATED);
    }

    @GetMapping("/students")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<StudentResponse>> listStudents(@RequestParam(required = false) Long classId) {
        if (classId != null) {
            return ResponseEntity.ok(userService.listStudentsByClass(classId));
        }
        return ResponseEntity.ok(userService.listStudents());
    }

    @GetMapping("/students/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getStudent(id));
    }

    // ---------- Parents ----------

    @PostMapping("/parents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParentResponse> createParent(@Valid @RequestBody CreateParentRequest request) {
        return new ResponseEntity<>(userService.createParent(request), HttpStatus.CREATED);
    }

    @GetMapping("/parents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ParentResponse>> listParents() {
        return ResponseEntity.ok(userService.listParents());
    }

    @GetMapping("/parents/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ResponseEntity<ParentResponse> getParent(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getParent(id));
    }

    @PostMapping("/link-parent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> linkParent(@Valid @RequestBody LinkParentRequest request) {
        userService.linkParentToStudent(request);
        return ResponseEntity.ok(new MessageResponse("Parent linked to student successfully"));
    }
}
