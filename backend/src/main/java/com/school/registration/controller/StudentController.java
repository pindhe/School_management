package com.school.registration.controller;

import com.school.registration.dto.common.MessageResponse;
import com.school.registration.dto.student.StudentRequest;
import com.school.registration.dto.student.StudentResponse;
import com.school.registration.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentResponse> list(@RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            return studentService.search(q);
        }
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public StudentResponse get(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(
            @Valid @RequestPart("student") StudentRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return studentService.create(request, photo);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentResponse update(
            @PathVariable Long id,
            @Valid @RequestPart("student") StudentRequest request,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        return studentService.update(id, request, photo);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        studentService.delete(id);
        return new MessageResponse("Student deleted successfully");
    }
}
