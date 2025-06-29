package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExportRequest {
    @NotBlank(message = "Export type is required")
    private String exportType; // "GRADES", "USERS", "SUBJECTS", "REPORTS"

    @NotBlank(message = "Format is required")
    private String format; // "EXCEL", "CSV", "PDF"

    private List<Long> entityIds;
    private Long semesterId;
    private List<String> columns;
    private Boolean includeHeaders = true;

}
