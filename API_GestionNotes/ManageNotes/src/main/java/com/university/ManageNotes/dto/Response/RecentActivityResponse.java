package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecentActivityResponse {
    private Long id;
    private String type;  // "grade", "claim", "user"
    private String description;
    private LocalDateTime timestamp;
    private String user;
}
