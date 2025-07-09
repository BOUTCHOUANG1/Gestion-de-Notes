package com.university.ManageNotes.dto.Response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private BigDecimal credits;
    private String description;
    private Boolean active;
}
