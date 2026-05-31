package com.school.sms.controller;

import com.school.sms.dto.announcement.AnnouncementRequest;
import com.school.sms.dto.announcement.AnnouncementResponse;
import com.school.sms.dto.common.MessageResponse;
import com.school.sms.security.SecurityUtils;
import com.school.sms.security.UserPrincipal;
import com.school.sms.service.AnnouncementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
@Tag(name = "Announcements", description = "Create and view announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<AnnouncementResponse> create(@Valid @RequestBody AnnouncementRequest request) {
        return new ResponseEntity<>(announcementService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AnnouncementResponse>> listAll() {
        return ResponseEntity.ok(announcementService.listAll());
    }

    @GetMapping("/feed")
    public ResponseEntity<List<AnnouncementResponse>> feed() {
        UserPrincipal principal = SecurityUtils.getCurrentPrincipal();
        return ResponseEntity.ok(
                announcementService.listForCurrentUser(principal.getUser().getRole().getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deactivate(@PathVariable Long id) {
        announcementService.deactivate(id);
        return ResponseEntity.ok(new MessageResponse("Announcement removed"));
    }
}
