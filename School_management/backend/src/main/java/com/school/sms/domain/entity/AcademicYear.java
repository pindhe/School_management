package com.school.sms.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "academic_years", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year_start", nullable = false)
    private Integer yearStart;

    @Column(name = "year_end", nullable = false)
    private Integer yearEnd;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = false;
}
