package com.school.sms.controller;

import com.school.sms.dto.academic.*;
import com.school.sms.dto.common.MessageResponse;
import com.school.sms.service.AcademicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic")
@Tag(name = "Academic Configuration", description = "Academic years, subjects, classes, assignments and timetables")
public class AcademicController {

    private final AcademicService academicService;

    public AcademicController(AcademicService academicService) {
        this.academicService = academicService;
    }

    // ---------- Academic Years ----------

    @PostMapping("/years")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AcademicYearResponse> createYear(@Valid @RequestBody AcademicYearRequest request) {
        return new ResponseEntity<>(academicService.createAcademicYear(request), HttpStatus.CREATED);
    }

    @GetMapping("/years")
    public ResponseEntity<List<AcademicYearResponse>> listYears() {
        return ResponseEntity.ok(academicService.listAcademicYears());
    }

    // ---------- Subjects ----------

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest request) {
        return new ResponseEntity<>(academicService.createSubject(request), HttpStatus.CREATED);
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<SubjectResponse>> listSubjects() {
        return ResponseEntity.ok(academicService.listSubjects());
    }

    // ---------- Classes ----------

    @PostMapping("/classes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassResponse> createClass(@Valid @RequestBody ClassRequest request) {
        return new ResponseEntity<>(academicService.createClass(request), HttpStatus.CREATED);
    }

    @GetMapping("/classes")
    public ResponseEntity<List<ClassResponse>> listClasses() {
        return ResponseEntity.ok(academicService.listClasses());
    }

    @GetMapping("/classes/{id}")
    public ResponseEntity<ClassResponse> getClass(@PathVariable Long id) {
        return ResponseEntity.ok(academicService.getClass(id));
    }

    @PostMapping("/classes/{id}/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassResponse> assignSubjects(@PathVariable Long id,
                                                        @Valid @RequestBody AssignSubjectsRequest request) {
        return ResponseEntity.ok(academicService.assignSubjectsToClass(id, request));
    }

    // ---------- Teacher Assignments ----------

    @PostMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherAssignmentResponse> assignTeacher(
            @Valid @RequestBody TeacherAssignmentRequest request) {
        return new ResponseEntity<>(academicService.assignTeacher(request), HttpStatus.CREATED);
    }

    @GetMapping("/assignments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherAssignmentResponse>> listAssignments() {
        return ResponseEntity.ok(academicService.listAllAssignments());
    }

    @GetMapping("/assignments/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<TeacherAssignmentResponse>> assignmentsForTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(academicService.listAssignmentsForTeacher(teacherId));
    }

    // ---------- Enrollment ----------

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> enroll(@Valid @RequestBody EnrollStudentRequest request) {
        academicService.enrollStudent(request);
        return ResponseEntity.ok(new MessageResponse("Student enrolled successfully"));
    }

    // ---------- Timetables ----------

    @PostMapping("/timetables")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimetableResponse> createTimetable(@Valid @RequestBody TimetableRequest request) {
        return new ResponseEntity<>(academicService.createTimetableEntry(request), HttpStatus.CREATED);
    }

    @GetMapping("/timetables/class/{classId}")
    public ResponseEntity<List<TimetableResponse>> timetableForClass(@PathVariable Long classId) {
        return ResponseEntity.ok(academicService.timetableForClass(classId));
    }

    @GetMapping("/timetables/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<List<TimetableResponse>> timetableForTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(academicService.timetableForTeacher(teacherId));
    }
}
