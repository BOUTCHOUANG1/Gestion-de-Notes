package com.university.ManageNotes.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// This class is a Data Transfer Object (DTO) for representing topic/subject information
// It contains fields for:
// - code: unique identifier for the topic
// - title: name of the topic/subject
// - cc: Continuous assessment score out of 30
// - sn: Session normale (normal session) score out of 70
// - semester: which semester the topic belongs to (s1 or s2)
// - credit: number of credits for this topic
// The class uses Lombok annotations (@Data, @Builder etc) to automatically generate
// getters, setters, constructors and other boilerplate code
public class TopicDto {
    private String code;
    private String title;
    private Double cc;   // Continuous assessment score (/30)
    private Double sn;   // Session normale score (/70)
    private String semester; // "s1" or "s2"
    private BigDecimal credit;
}