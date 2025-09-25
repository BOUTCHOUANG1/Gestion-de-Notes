package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.RevendicationRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.mapper.GradeClaimMapper;
import com.university.ManageNotes.model.Revendication;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.RevendicationRepository;
import com.university.ManageNotes.repository.GradeRepository;
import com.university.ManageNotes.repository.UserRepository;
import com.university.ManageNotes.service.RevendicationPeriodService;
import com.university.ManageNotes.service.RevendicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevendicationServiceImpl implements RevendicationService {

    private final RevendicationRepository claimRepository;
    private final GradeRepository gradeRepository;
    private final RevendicationPeriodService windowService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RevendicationPeriodResponse create(RevendicationRequest req) {
        var grade = gradeRepository.findById(req.getGradeId())
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        if (!windowService.isPeriodOpen(grade.getSemesters().getId(), grade.getExamPeriod())) {
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

        var currentUser = getCurrentUser();
        claim.setCreatedBy(currentUser);

        return gradeClaimMapper.toResponse(claimRepository.save(claim));
    }

    @Override
    public List<RevendicationPeriodResponse> listClaimsForTeacher(Long teacherId) {
        return claimRepository.findByGrade_Subject_IdTeacher(teacherId).stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    @Override
    public List<RevendicationPeriodResponse> listAll() {
        return claimRepository.findAll().stream()
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    @Override
    public List<RevendicationPeriodResponse> getClaimsForCurrentTeacher() {
        Users user = getCurrentUser();
        return listClaimsForTeacher(user.getId());
    }

    @Override
    public List<RevendicationPeriodResponse> getPending() {
        return claimRepository.findAll().stream()
                .filter(claim -> claim.getStatus() == null || claim.getStatus().equals("PENDING"))
                .map(gradeClaimMapper::toResponse)
                .toList();
    }

    @Override
    public RevendicationPeriodResponse getById(Long id) {
        var claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revendication not found"));
        return gradeClaimMapper.toResponse(claim);
    }

    private Users getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserPrincipal) {
            var userPrincipal = (UserPrincipal) auth.getPrincipal();
            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("No authenticated user");
    }
}