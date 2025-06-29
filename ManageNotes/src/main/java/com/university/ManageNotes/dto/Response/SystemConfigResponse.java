package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfigResponse extends AbstractEntity {
    private String configKey;
    private String configValue;
    private String description;
    private String category;

    private Long updatedBy;
    private String updatedByName;

}
