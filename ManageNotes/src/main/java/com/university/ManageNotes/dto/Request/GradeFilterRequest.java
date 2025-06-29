package com.university.ManageNotes.dto.Request;

import com.university.ManageNotes.model.GradeType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GradeFilterRequest {


    private List<Long> studentIds;
    private List<Long> subjectIds;
    private Long semesterId;
    private List<GradeType> gradeTypes;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "20.0")
    private Double minGrade;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "20.0")
    private Double maxGrade;



    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "enteredAt";
    private String sortDirection = "DESC";



}
