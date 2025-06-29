package com.university.ManageNotes.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfigRequest {

    @NotBlank(message = "Configuration key is required")
    private String configKey;

    @NotBlank(message = "Configuration value is required")
    private String configValue;

    private String description;
    private String category;


}
