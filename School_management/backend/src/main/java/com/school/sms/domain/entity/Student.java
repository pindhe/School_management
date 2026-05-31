package com.school.sms.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id"),
        @UniqueConstraint(columnNames = "admission_number")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "admission_number", nullable = false, length = 30)
    private String admissionNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_class_id")
    private SchoolClass currentClass;

    @Column(name = "date_of_admission")
    private LocalDate dateOfAdmission;
}
