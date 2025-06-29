package com.university.ManageNotes.dto.Response;

import com.university.ManageNotes.model.DataSeries;

import java.util.List;
import java.util.Map;

public class AnalyticsResponse {
    private String title;
    private String description;
    private String chartType; // "LINE", "BAR", "PIE", "AREA"
    private List<String> labels;
    private List<DataSeries> datasets;
    private Map<String, Object> summary;

}
