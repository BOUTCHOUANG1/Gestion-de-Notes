package com.university.ManageNotes.controller;

// ... existing code ... <imports>
import com.university.ManageNotes.dto.Request.BulkExportRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.service.BulkExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Bulk Export", description = "Generate or publish bulk documents")
public class ExportController {

    private final BulkExportService service;

    @PostMapping("/print")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate PDFs based on filters (Imprimer)")
    public MessageResponse print(@Valid @RequestBody BulkExportRequest request) {
        return service.export(request, false);
    }

    @PostMapping("/publish")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Publish selected items")
    public MessageResponse publish(@Valid @RequestBody BulkExportRequest request) {
        return service.export(request, true);
    }
}
