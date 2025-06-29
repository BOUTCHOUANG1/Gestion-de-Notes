package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {

    private String fileName;
    private String uploadId;
    private Integer totalRows;
    private Integer successfulRows;
    private Integer errorRows;
    private List<String> errors;
    private String downloadUrl;
    private Boolean validationOnly;

}
