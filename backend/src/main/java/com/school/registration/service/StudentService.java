package com.school.registration.service;

import com.school.registration.domain.entity.Student;
import com.school.registration.dto.student.StudentRequest;
import com.school.registration.dto.student.StudentResponse;
import com.school.registration.exception.BadRequestException;
import com.school.registration.exception.ResourceNotFoundException;
import com.school.registration.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public List<StudentResponse> findAll() {
        return studentRepository.findAll().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::toResponse)
                .toList();
    }

    public List<StudentResponse> search(String query) {
        if (!StringUtils.hasText(query)) {
            return findAll();
        }
        return studentRepository.search(query.trim()).stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse findById(Long id) {
        return toResponse(getStudent(id));
    }

    @Transactional
    public StudentResponse create(StudentRequest request, MultipartFile photo) {
        validateUniqueEmail(request.getEmail(), null);
        Student student = mapRequest(new Student(), request);
        if (photo != null && !photo.isEmpty()) {
            student.setPhotoPath(storePhoto(photo));
        }
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request, MultipartFile photo) {
        Student student = getStudent(id);
        validateUniqueEmail(request.getEmail(), id);
        mapRequest(student, request);
        if (photo != null && !photo.isEmpty()) {
            deletePhotoIfExists(student.getPhotoPath());
            student.setPhotoPath(storePhoto(photo));
        }
        return toResponse(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        Student student = getStudent(id);
        deletePhotoIfExists(student.getPhotoPath());
        studentRepository.delete(student);
    }

    public Path resolvePhotoPath(String filename) {
        return Paths.get(uploadDir).resolve(filename).normalize();
    }

    private Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private void validateUniqueEmail(String email, Long excludeId) {
        boolean exists = excludeId == null
                ? studentRepository.existsByEmail(email.trim().toLowerCase())
                : studentRepository.existsByEmailAndIdNot(email.trim().toLowerCase(), excludeId);
        if (exists) {
            throw new BadRequestException("A student with this email already exists");
        }
    }

    private Student mapRequest(Student student, StudentRequest request) {
        student.setName(request.getName().trim());
        student.setAddress(request.getAddress().trim());
        student.setPhone(request.getPhone().trim());
        student.setEmail(request.getEmail().trim().toLowerCase());
        return student;
    }

    private String storePhoto(MultipartFile photo) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);
            String extension = StringUtils.getFilenameExtension(photo.getOriginalFilename());
            String filename = UUID.randomUUID() + (extension != null ? "." + extension : ".jpg");
            Path target = uploadPath.resolve(filename);
            Files.copy(photo.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store photo: " + ex.getMessage());
        }
    }

    private void deletePhotoIfExists(String photoPath) {
        if (!StringUtils.hasText(photoPath)) {
            return;
        }
        try {
            Files.deleteIfExists(Paths.get(uploadDir).resolve(photoPath));
        } catch (IOException ignored) {
            // non-critical cleanup
        }
    }

    public StudentResponse toResponse(Student student) {
        String photoUrl = StringUtils.hasText(student.getPhotoPath())
                ? "/api/photos/" + student.getPhotoPath()
                : null;
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .address(student.getAddress())
                .phone(student.getPhone())
                .email(student.getEmail())
                .photoUrl(photoUrl)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
