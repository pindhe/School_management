package com.school.sms.domain.entity;

import com.school.sms.domain.enums.TargetRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "published_by_user_id", nullable = false)
    private User publishedBy;

    @CreationTimestamp
    @Column(name = "publish_date", updatable = false)
    private LocalDateTime publishDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role", length = 10)
    private TargetRole targetRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "target_class_id")
    private SchoolClass targetClass;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
