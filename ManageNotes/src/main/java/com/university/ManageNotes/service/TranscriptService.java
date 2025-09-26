package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Response.TranscriptResponse;
import com.university.ManageNotes.model.TeachingLevel;

import java.util.List;

public interface TranscriptService {
    TranscriptResponse getTranscriptStudent(Long studentId);

    List<TranscriptResponse> getTranscriptByTeachingLevel(TeachingLevel level);

}
