package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.model.Exam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevendicationPeriodResponse {
    private Long id;
    private String name;
    private String shortName;
    private Exam periodLabel;
    private Integer semester;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isActive;
    private Integer order;
    private Instant createdDate;
    private Instant lastModifiedDate;

    private List<RevendicationPeriodRequest> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}