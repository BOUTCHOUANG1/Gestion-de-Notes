package com.university.ManageNotes.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class GradingWindow extends AbstractEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_label")
    private PeriodType periodLabel;

    @ManyToOne
    @JoinColumn(name = "id_semester")
    private Semesters semester;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "color")
    private String color;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "order_index")
    private Integer order;

    // Removed duplicate field
}
