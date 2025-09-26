package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.model.Transcript;
import com.university.ManageNotes.repository.TranscriptRepository;
import com.university.ManageNotes.service.TeacherService;
import com.university.ManageNotes.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranscriptServiceImpl implements TranscriptService {
    private TranscriptRepository transcriptRepository;
    private ModelMapper modelMapper;

}
