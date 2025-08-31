package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private BigDecimal credits;
    private String description;
    private Boolean active;
    private StudentLevel level;
    private StudentCycle cycle;
    private Long semesterId;
    private String semesterName;
    private Long departmentId;
    private String departmentName;
    private Long teacherId;
    private String teacherName;
    private java.time.Instant createdDate;
    private java.time.Instant lastModifiedDate;
}
