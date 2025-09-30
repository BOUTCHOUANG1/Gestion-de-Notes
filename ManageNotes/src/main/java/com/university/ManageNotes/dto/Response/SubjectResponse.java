package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.TeachingLevel;
import com.university.ManageNotes.model.enums.StudentCycle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private BigDecimal credits;
    private String description;
    private TeacherResponse teacher;
    private Set<TeachingLevel> subjectsLevel;
    private StudentCycle studentCycle;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private SemesterResponse semester;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private DepartmentResponse department;

    private Set<SubjectResponse> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
