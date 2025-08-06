package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "student_info_requests")
public class StudentInfoRequest extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String requestedChangesJson; // JSON payload of requested fields & values

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private String rejectionReason;

    private LocalDateTime createdAt = LocalDateTime.now();
}
