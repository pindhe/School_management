package com.school.sms.service;

import com.school.sms.domain.entity.Announcement;
import com.school.sms.domain.entity.SchoolClass;
import com.school.sms.domain.entity.User;
import com.school.sms.domain.enums.RoleName;
import com.school.sms.domain.enums.TargetRole;
import com.school.sms.dto.announcement.AnnouncementRequest;
import com.school.sms.dto.announcement.AnnouncementResponse;
import com.school.sms.exception.ResourceNotFoundException;
import com.school.sms.repository.AnnouncementRepository;
import com.school.sms.repository.SchoolClassRepository;
import com.school.sms.repository.UserRepository;
import com.school.sms.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final DtoMapper mapper;

    public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository,
                               SchoolClassRepository schoolClassRepository, DtoMapper mapper) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AnnouncementResponse create(AnnouncementRequest req) {
        User publisher = userRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", SecurityUtils.getCurrentUserId()));
        SchoolClass targetClass = null;
        if (req.targetClassId() != null) {
            targetClass = schoolClassRepository.findById(req.targetClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", req.targetClassId()));
        }
        Announcement announcement = Announcement.builder()
                .title(req.title())
                .content(req.content())
                .publishedBy(publisher)
                .targetRole(req.targetRole() != null ? req.targetRole() : TargetRole.ALL)
                .targetClass(targetClass)
                .active(true)
                .build();
        return mapper.toAnnouncementResponse(announcementRepository.save(announcement));
    }

    public List<AnnouncementResponse> listAll() {
        return announcementRepository.findByActiveTrueOrderByPublishDateDesc().stream()
                .map(mapper::toAnnouncementResponse).toList();
    }

    public List<AnnouncementResponse> listForCurrentUser(RoleName role) {
        TargetRole roleTarget = TargetRole.valueOf(role.name());
        return announcementRepository
                .findByActiveTrueAndTargetRoleInOrderByPublishDateDesc(List.of(TargetRole.ALL, roleTarget)).stream()
                .map(mapper::toAnnouncementResponse).toList();
    }

    @Transactional
    public void deactivate(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Announcement", id));
        announcement.setActive(false);
        announcementRepository.save(announcement);
    }
}
