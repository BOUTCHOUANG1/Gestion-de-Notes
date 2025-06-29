package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadRequest {

    @NotNull(message = "File is required")
    private org.springframework.web.multipart.MultipartFile file;

    @NotBlank(message = "Upload type is required")
    private String uploadType; // "GRADES", "USERS", "SUBJECTS"

    private Long semesterId;
    private Boolean skipErrors = false;
    private Boolean validateOnly = false;


}
