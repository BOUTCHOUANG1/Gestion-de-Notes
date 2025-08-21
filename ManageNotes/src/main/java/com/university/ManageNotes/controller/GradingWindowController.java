package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.GradingWindowRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.GradingWindow;
import com.university.ManageNotes.repository.GradingWindowRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grading-windows")
@RequiredArgsConstructor
public class GradingWindowController {
    private final GradingWindowRepository windowRepository;
    private final SemesterRepository semesterRepository;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create or update a grading window (admin only)")
    public ResponseEntity<?> createOrUpdate(@Valid @RequestBody GradingWindowRequest req){
        var semester = semesterRepository.findById(req.getSemesterId())
                .orElseThrow(()-> new RuntimeException("Semester not found"));
        GradingWindow window = windowRepository.findBySemesterIdAndNameIgnoreCase(req.getSemesterId(), req.getName())
                .stream().findFirst().orElse(new GradingWindow());
        window.setSemester(semester);
        window.setName(req.getName());
        window.setStartDate(req.getStartDate());
        window.setEndDate(req.getEndDate());
        window.setActive(req.getActive());
        windowRepository.save(window);
        return ResponseEntity.ok(MessageResponse.success("Window saved"));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Toggle active flag of a grading window (admin)")
    public ResponseEntity<?> toggle(@PathVariable Long id){
        var window = windowRepository.findById(id).orElseThrow(() -> new RuntimeException("Window not found"));
        window.setActive(!Boolean.TRUE.equals(window.getActive()));
        windowRepository.save(window);
        return ResponseEntity.ok(MessageResponse.success("Window toggled"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all grading windows (admin)")
    public List<GradingWindow> list(){
        return windowRepository.findAll();
    }
}
