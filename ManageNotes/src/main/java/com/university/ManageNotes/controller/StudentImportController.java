package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.DbSyncRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.service.StudentImportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students/import")
@PreAuthorize("hasRole('ADMIN')")
public class StudentImportController {

    @Autowired
    private StudentImportService importService;

    @PostMapping("/file")
    @Operation(summary = "Import students from Excel/CSV")
    public ResponseEntity<List<MessageResponse>> importFile(@RequestParam("file") MultipartFile file) throws Exception {
        String type = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
        List<MessageResponse> resp;
        if (type.endsWith(".xlsx") || type.endsWith(".xls")) {
            resp = importService.importFromExcel(file);
        } else if (type.endsWith(".csv")) {
            resp = importService.importFromCsv(file);
        } else {
            return ResponseEntity.badRequest().body(List.of(MessageResponse.error("Unsupported file format")));
        }
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/external-db")
    @Operation(summary = "Sync students from external PostgreSQL database")
    public ResponseEntity<List<MessageResponse>> syncExternal(@RequestBody DbSyncRequest request) throws Exception {
        List<MessageResponse> resp = importService.importFromExternalDb(request.getJdbcUrl(), request.getUsername(), request.getPassword(), request.getQuery());
        return ResponseEntity.ok(resp);
    }
}
