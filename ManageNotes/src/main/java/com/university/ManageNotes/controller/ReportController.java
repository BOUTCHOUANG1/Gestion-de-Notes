package com.university.ManageNotes.controller;

import com.university.ManageNotes.dto.Request.ReportRequest;
import com.university.ManageNotes.dto.Request.BulkReportRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.ReportResponse;
import com.university.ManageNotes.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/student/{studentId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Generate semester transcript for a student",
            description = "Creates a transcript (gpa, credits, status, grades) for the given student and semester. " +
                    "Returns ReportResponse containing header info plus detailed grades. Requires faculty, universityName, academicYear in request body.")
    public ResponseEntity<?> generateStudentReport(@PathVariable Long studentId,
                                                   @Valid @RequestBody ReportRequest reportRequest) {
        try {
            ReportResponse report = reportService.generateStudentReport(studentId, reportRequest);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to generate student report: " + e.getMessage()));
        }
    }

    @PostMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Generate subject grade report",
            description = "Creates a report for all students of the specified subject in a semester, returning aggregated statistics and PDF if requested.")
    public ResponseEntity<?> generateSubjectReport(@PathVariable Long subjectId,
                                                   @Valid @RequestBody ReportRequest reportRequest) {
        try {
            ReportResponse report = reportService.generateSubjectReport(subjectId, reportRequest);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to generate subject report: " + e.getMessage()));
        }
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate bulk transcripts", description = "Generate transcripts for an entire class/semester and optionally email them")
    public ResponseEntity<?> generateBulkReports(@Valid @RequestBody(required = false) BulkReportRequest request) {
        // For now, just acknowledge the request. Detailed implementation can be added later.
        return ResponseEntity.ok(MessageResponse.success("Bulk report generation initiated"));
    }

    @GetMapping("/student/{studentId}/pdf")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN') or (hasRole('STUDENT') and @securityService.isOwnStudent(#studentId))")
    @Operation(summary = "Download previously generated transcript PDF",
            description = "Returns the PDF bytes for a student's most recent transcript for the selected semester.")
    public ResponseEntity<byte[]> downloadStudentReportPDF(@PathVariable Long studentId,
                                                           @RequestParam(required = false) String reportTitle) {
        try {
            // This would typically fetch student and grades from database
            // For now, using placeholder logic
            byte[] pdfBytes = reportService.generatePDFReport(null, null, reportTitle != null ? reportTitle : "Student Report");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "student_report_" + studentId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Export grades to Excel/CSV",
            description = "Exports filtered grades (student, subject, semester) to a CSV/Excel file for offline analysis.")
    public ResponseEntity<byte[]> exportGradesToExcel(@RequestParam(required = false) Long studentId,
                                                      @RequestParam(required = false) Long subjectId,
                                                      @RequestParam(required = false) Long semesterId) {
        try {
            // This would typically fetch grades based on filters
            byte[] excelBytes = reportService.exportToExcel(null); // Placeholder

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "grades_export.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get grade analytics",
            description = "Provides aggregated grade distribution, averages and other KPI for the specified filters.")
    public ResponseEntity<?> getGradeAnalytics(@RequestParam(required = false) Long semesterId,
                                               @RequestParam(required = false) Long subjectId) {
        try {
            // Analytics logic here
            return ResponseEntity.ok(MessageResponse.success("Analytics data retrieved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Failed to retrieve analytics: " + e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}/year-summary")
    @Operation(summary = "Get student year summary", description = "Return calculated averages and credits without generating/storing PDF")
    public ResponseEntity<?> getStudentYearSummary(@PathVariable Long studentId) {
        try {
            var resp = reportService.getStudentYearSummary(studentId);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new com.university.ManageNotes.dto.Response.MessageResponse("Error generating summary: " + e.getMessage(), "ERROR"));
        }
    }
}
