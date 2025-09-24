package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.mapper.GradeClaimMapper;
import com.university.ManageNotes.model.Revendication;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.RevendicationRepository;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeClaimService extends AbstractRequestService<Revendication, RevendicationPeriodResponse, RevendicationRequest> {

    private final RevendicationRepository claimRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final RevendicationPeriodService windowService;
    private final UserRepository userRepository;
    private final GradeClaimMapper gradeClaimMapper;

    public GradeClaimService(RevendicationRepository claimRepository,
                             GradeRepository gradeRepository,
                             SubjectRepository subjectRepository,
                             RevendicationPeriodService windowService,
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
    public RevendicationPeriodResponse create(RevendicationRequest req) {
        var grade = gradeRepository.findById(req.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (!windowService.isWindowOpen(grade.getSemesters()
                .getId(), grade.getExamPeriod())) {
            throw new RuntimeException("Claim period is closed");
        }

        String period = String.valueOf(req.getPeriod());
        if(period==null || (!period.equals("CC_1") && !period.equals("CC_2") && !period.equals("SN_1") && !period.equals("SN_2"))){
            throw new RuntimeException("period must be CC_1, CC_2, SN_1 or SN_2");
        }

        Revendication claim = new Revendication();
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

    public List<RevendicationPeriodResponse> listClaimsForTeacher(Long teacherId) {
        return claimRepository.findByGrade_Subject_IdTeacher(teacherId).stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    public List<RevendicationPeriodResponse> listAll() {
        return claimRepository.findAll().stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    public List<RevendicationPeriodResponse> getClaimsForCurrentTeacher() {
        Users user = getCurrentUser();
        return listClaimsForTeacher(user.getId());
    }

    @Override
    protected void onApprove(Revendication claim) {
        // This is called when a claim is approved
        var grade = claim.getGrade();
        grade.setScore(claim.getRequestedScore());
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
    public RevendicationPeriodResponse toDto(Revendication entity) {
        return gradeClaimMapper.toResponse(entity);
    }

    @Override
    public List<RevendicationPeriodResponse> getPending() {
        return super.getPending();
    }

    @Override
    public RevendicationPeriodResponse getById(Long id) {
        return super.getById(id);
    }
}
