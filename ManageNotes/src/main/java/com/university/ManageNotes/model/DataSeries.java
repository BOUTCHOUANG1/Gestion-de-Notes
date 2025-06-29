package com.university.ManageNotes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataSeries extends AbstractEntity{
    private String label;
    private List<Double> data;
    private String color;

}
