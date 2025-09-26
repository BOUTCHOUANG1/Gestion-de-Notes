package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.RevendicationPeriodRequest;
import com.university.ManageNotes.dto.Response.RevendicationPeriodResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.model.Exam;
import com.university.ManageNotes.model.RevendicationPeriod;
import com.university.ManageNotes.repository.RevendicationPeriodRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.service.RevendicationPeriodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevendicationPeriodServiceImpl implements RevendicationPeriodService {
    private final RevendicationPeriodRepository periodRepository;
    private final SemesterRepository semesterRepository;
    private final ModelMapper modelMapper;

    @Override
    public Boolean isPeriodOpen(Long semesterId, Exam exam) {
        if (exam == null) return true;
        List<RevendicationPeriod> period = periodRepository.findBySemesterIdAndExamPeriod(semesterId, exam);
        if (period.isEmpty()){
            return false;
        }
        LocalDate today = LocalDate.now();
        return period.stream()
                .anyMatch(p -> Boolean.TRUE.equals(p.getIsActive()) &&
                (p.getStartDate()==null || !today.isBefore(p.getStartDate())) &&
                (p.getEndDate()==null || !today.isAfter(p.getEndDate())));
    }

    @Override
    public String getPeriodStatusMessage(Long semesterId, Exam exam) {
        if (exam == null) return "No revendication period specified - entry allowed";

        List<RevendicationPeriod> period = periodRepository.findBySemesterIdAndExamPeriod(semesterId, exam);
        if (period.isEmpty()) {
            return "Revendication period for " + exam + " not found";
        }

        RevendicationPeriod periodDb = period.getFirst();
        LocalDate today = LocalDate.now();

        if (!Boolean.TRUE.equals(periodDb.getIsActive())) {
            return "Revendication period '" + exam + "' is disabled by administrator";
        }

        if (periodDb.getStartDate() != null && today.isBefore(periodDb.getStartDate())) {
            return "Revendication period '" + exam + "' opens on " + periodDb.getStartDate();
        }

        if (periodDb.getEndDate() != null && today.isAfter(periodDb.getEndDate())) {
            return "Revendication period '" + exam + "' closed on " + periodDb.getEndDate();
        }

        return "Revendication period '" + exam + "' is open";
    }

    @Override
    public List<RevendicationPeriodResponse> getAllPeriods() {
        List<RevendicationPeriod> revendicationPeriod = this.periodRepository.findAll();

        if(revendicationPeriod.isEmpty()){
            throw new APIException("No revendication period were defined");
        }

        List<RevendicationPeriodRequest> periodRequests = revendicationPeriod.stream()
                .map(p -> modelMapper.map(p, RevendicationPeriodRequest.class))
                .toList();

        RevendicationPeriodResponse response = new RevendicationPeriodResponse();
        response.setPeriodLabel(periodRequest);

        return periodRepository.findAllByOrderByOrderAsc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
