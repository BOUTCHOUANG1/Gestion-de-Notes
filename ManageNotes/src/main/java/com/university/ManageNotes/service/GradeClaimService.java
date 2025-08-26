package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.GradeClaimRequest;
import com.university.ManageNotes.dto.Response.GradeClaimResponse;
import com.university.ManageNotes.mapper.GradeClaimMapper;
import com.university.ManageNotes.model.GradeClaim;
import com.university.ManageNotes.model.Grades;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.GradeClaimRepository;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.security.UserPrincipal;
import com.university.ManageNotes.service.GradingWindowService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
public class GradeClaimService extends AbstractRequestService<GradeClaim, GradeClaimResponse, GradeClaimRequest> {

    private final GradeClaimRepository claimRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final GradingWindowService windowService;
    private final UserRepository userRepository;
    private final GradeClaimMapper gradeClaimMapper;

    public GradeClaimService(GradeClaimRepository claimRepository,
                             GradeRepository gradeRepository,
                             SubjectRepository subjectRepository,
                             GradingWindowService windowService,
                             UserRepository userRepository,
                             GradeClaimMapper gradeClaimMapper) {
        super(claimRepository, gradeClaimMapper::toResponse);
        this.claimRepository = claimRepository;
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.windowService = windowService;
        this.userRepository = userRepository;
        this.gradeClaimMapper = gradeClaimMapper;
    }

    @Override
    @Transactional
    public GradeClaimResponse create(GradeClaimRequest req) {
        var grade = gradeRepository.findById(req.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (!windowService.isWindowOpen(grade.getSemesters()
                .getId(), grade.getPeriodType())) {
            throw new RuntimeException("Claim period is closed");
        }

        String period = String.valueOf(req.getPeriod());
        if(period==null || (!period.equals("CC_1") && !period.equals("CC_2") && !period.equals("SN_1") && !period.equals("SN_2"))){
            throw new RuntimeException("period must be CC_1, CC_2, SN_1 or SN_2");
        }

        GradeClaim claim = new GradeClaim();
        claim.setStudent(grade.getStudent());
        claim.setGrade(grade);
        claim.setSemester(grade.getSemesters());
        claim.setPeriodLabel(period);
        claim.setRequestedScore(req.getRequestedScore());
        claim.setCause(req.getCause());
        claim.setDescription(req.getDescription());

        // Set created by from security context
        var currentUser = getCurrentUser();
        claim.setCreatedBy(currentUser);

        return gradeClaimMapper.toResponse(claimRepository.save(claim));
    }

    public List<GradeClaimResponse> listClaimsForTeacher(Long teacherId) {
        return claimRepository.findByGrade_Subject_IdTeacher(teacherId).stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    public List<GradeClaimResponse> listAll() {
        return claimRepository.findAll().stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    public List<GradeClaimResponse> getClaimsForCurrentTeacher() {
        Users user = getCurrentUser();
        return listClaimsForTeacher(user.getId());
    }

    @Override
    protected void onApprove(GradeClaim claim) {
        // This is called when a claim is approved
        var grade = claim.getGrade();
        grade.setValue(claim.getRequestedScore());
        gradeRepository.save(grade);
    }

    // MapStruct handles the DTO conversion through the mapper interface

    private Users getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            var userPrincipal = (UserPrincipal) auth.getPrincipal();
            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user");
    }

    // Implement any additional methods required by the abstract class or interface
    @Override
    public GradeClaimResponse toDto(GradeClaim entity) {
        return gradeClaimMapper.toResponse(entity);
    }

    @Override
    public List<GradeClaimResponse> getPending() {
        return super.getPending();
    }

    @Override
    public GradeClaimResponse getById(Long id) {
        return super.getById(id);
    }
}
