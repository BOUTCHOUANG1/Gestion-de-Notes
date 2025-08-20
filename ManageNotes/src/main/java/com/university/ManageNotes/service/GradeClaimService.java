package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeClaimRequest;
import com.university.ManageNotes.model.GradeClaim;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeClaimService {
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final GradeClaimRepository claimRepository;
    private final GradingWindowService windowService;

    public GradeClaim createClaim(Long studentId, GradeClaimRequest req) {
        var grade = gradeRepository.findById(req.getGradeId()).orElseThrow();
        if (!grade.getStudent().getId().equals(studentId)) throw new RuntimeException("Grade not owned by student");
        var semester = grade.getSemesters();
        if (!windowService.isWindowOpen(semester.getId(), grade.getPeriodLabel())) {
            throw new RuntimeException("Claim period closed");
        }
        GradeClaim claim = new GradeClaim();
        claim.setStudent(grade.getStudent());
        claim.setGrade(grade);
        claim.setSemester(semester);
        claim.setPeriodLabel(grade.getPeriodLabel());
        claim.setRequestedScore(req.getRequestedScore());
        claim.setCause(req.getCause());
        claim.setDescription(req.getDescription());
        claim.setStatus(GradeClaim.ClaimStatus.PENDING);
        return claimRepository.save(claim);
    }

    public List<GradeClaim> listClaimsForTeacher(Long teacherId) {
        return claimRepository.findByGrade_Subject_IdIn(
                subjectRepository.findByIdTeacher(teacherId).stream().map(s->s.getId()).toList());
    }

    public List<GradeClaim> listAll() {
        return claimRepository.findAll();
    }

    public GradeClaim decide(Long claimId, boolean approve, String comment) {
        var claim = claimRepository.findById(claimId).orElseThrow();
        if (claim.getStatus() != GradeClaim.ClaimStatus.PENDING) {
            throw new RuntimeException("Claim already decided");
        }
        claim.setStatus(approve ? GradeClaim.ClaimStatus.APPROVED : GradeClaim.ClaimStatus.REJECTED);
        claim.setTeacherComment(comment);
        claim.setResolvedAt(LocalDateTime.now());
        if (approve) {
            var grade = claim.getGrade();
            grade.setValue(claim.getRequestedScore());
            gradeRepository.save(grade);
        }
        return claimRepository.save(claim);
    }
}
