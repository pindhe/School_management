package com.school.sms.repository;

import com.school.sms.domain.entity.Announcement;
import com.school.sms.domain.enums.TargetRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByActiveTrueOrderByPublishDateDesc();

    List<Announcement> findByActiveTrueAndTargetRoleInOrderByPublishDateDesc(List<TargetRole> roles);
}
