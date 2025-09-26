package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
}
