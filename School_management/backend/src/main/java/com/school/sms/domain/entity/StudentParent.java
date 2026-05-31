package com.school.sms.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_parents", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "parent_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @Column(length = 30)
    private String relationship;
}
