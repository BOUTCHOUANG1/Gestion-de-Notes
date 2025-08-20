package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {
    private String code;
    private String title;
    private Double cc;   // Continuous assessment score (/30)
    private Double sn;   // Session normale score (/70)
    private String semester; // "s1" or "s2"
    private java.math.BigDecimal credit;
}
