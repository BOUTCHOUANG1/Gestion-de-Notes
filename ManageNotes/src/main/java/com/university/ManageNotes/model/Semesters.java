package com.university.ManageNotes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Semesters extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active")
    private Boolean active;

    /**
     * Custom ordering index managed by administrators.
     * A lower value means the period appears earlier in the list.  Nullable for backward-compatibility;
     * when null, ordering falls back to {@code startDate}.
     */
    @Column(name = "order_index")
    private Integer orderIndex;

    @JsonIgnore
    @OneToMany(mappedBy = "semester", fetch = FetchType.LAZY)
    private List<Grades> grades;
}
