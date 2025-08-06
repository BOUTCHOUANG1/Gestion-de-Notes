package com.university.ManageNotes.dto.Request;

// ... existing code ... <imports>
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkExportRequest {
    @NotBlank
    private String level; // L1, L2 …
    private Long subjectId;
    @NotBlank
    private String documentType; // NOTES, STUDENTS, TEACHERS
    private String periodLabel; // CC1, SN1, CC2, SN2 – optional when documentType!=NOTES
    private Long semesterId; // optional
}
