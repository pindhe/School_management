package com.school.sms.controller;

import com.school.sms.dto.common.MessageResponse;
import com.school.sms.dto.grade.GradeRequest;
import com.school.sms.dto.grade.GradeResponse;
import com.school.sms.service.GradeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grades", description = "Record and view student grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<GradeResponse> record(@Valid @RequestBody GradeRequest request) {
        return new ResponseEntity<>(gradeService.recordGrade(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.ok(new MessageResponse("Grade deleted successfully"));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseEntity<List<GradeResponse>> studentGrades(
            @PathVariable Long studentId,
            @RequestParam(required = false) Long academicYearId) {
        if (academicYearId != null) {
            return ResponseEntity.ok(gradeService.getStudentGradesByYear(studentId, academicYearId));
        }
        return ResponseEntity.ok(gradeService.getStudentGrades(studentId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<GradeResponse>> myGrades() {
        return ResponseEntity.ok(gradeService.getMyGrades());
    }
}
