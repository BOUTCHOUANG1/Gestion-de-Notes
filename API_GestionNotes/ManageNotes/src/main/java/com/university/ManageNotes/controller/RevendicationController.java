package com.university.ManageNotes.controller;

import com.university.ManageNotes.config.AppConstant;
import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.RevendicationResponse;
import com.university.ManageNotes.service.RevendicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Revendication Management", description = "Grade revendication operations")
public class RevendicationController {
    private final RevendicationService revendicationService;

    @PostMapping(value = "/student/revendication", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create grade revendication (Student)", description = "Student submits a grade revendication request with optional proof image")
    public ResponseEntity<RevendicationResponse> createRevendication(
            @Valid @RequestPart("request") RevendicationRequest request,
            @RequestPart(value = "proof", required = false) MultipartFile proofFile) {
         return new ResponseEntity<>(revendicationService.createRevendication(request, proofFile), HttpStatus.CREATED);
     }

    @GetMapping("/student/revendications")
    @Operation(summary = "Get my revendications (Student)", description = "Student views their own revendications")
    public ResponseEntity<List<RevendicationResponse>> getMyRevendications() {
        return new ResponseEntity<>(revendicationService.getMyRevendications(), HttpStatus.OK);
    }

    @GetMapping("/teacher/revendications")
    @Operation(summary = "Get pending revendications (Teacher)", description = "Teacher views pending revendications for their subjects")
    public ResponseEntity<List<RevendicationResponse>> getRevendicationsForTeacher(
            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_REVENDICATION_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIR, required = false) String sortOrder) {
        return new ResponseEntity<>(revendicationService.getRevendicationForTeacher(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/teacher/revendication/{id}/approve")
    @Operation(summary = "Approve revendication (Teacher)", description = "Teacher approves a grade revendication")
    public ResponseEntity<MessageResponse> approveRevendication(
            @PathVariable Long id,
            @RequestParam(required = false) String comment) {
        return new ResponseEntity<>(revendicationService.approveRevendication(id, comment), HttpStatus.OK);
    }

    @PostMapping("/teacher/revendication/{id}/reject")
    @Operation(summary = "Reject revendication (Teacher)", description = "Teacher rejects a grade revendication")
    public ResponseEntity<MessageResponse> rejectRevendication(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        return new ResponseEntity<>(revendicationService.rejectRevendication(id, reason), HttpStatus.OK);
    }

    @GetMapping("/admin/revendications")
    @Operation(summary = "Get all revendications (Admin)", description = "Admin views all revendications")
    public ResponseEntity<List<RevendicationResponse>> getAllRevendications() {
        return new ResponseEntity<>(revendicationService.getAllRevendications(), HttpStatus.OK);
    }
}