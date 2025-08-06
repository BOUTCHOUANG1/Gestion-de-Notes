package com.university.ManageNotes.service;

// ... existing code ... <imports>
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.RequestStatus;
import com.university.ManageNotes.model.StudentInfoRequest;
import com.university.ManageNotes.model.Students;
import com.university.ManageNotes.repository.StudentInfoRequestRepository;
import com.university.ManageNotes.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentInfoRequestService {

    private final StudentInfoRequestRepository requestRepository;
    private final StudentRepository studentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<StudentInfoRequest> getPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }

    public Optional<StudentInfoRequest> getRequest(Long id) {
        return requestRepository.findById(id);
    }

    @Transactional
    public MessageResponse approve(Long id) {
        return requestRepository.findById(id)
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .map(req -> {
                    Students student = req.getStudent();
                    applyChanges(student, req.getRequestedChangesJson());
                    studentRepository.save(student);
                    req.setStatus(RequestStatus.APPROVED);
                    requestRepository.save(req);
                    return MessageResponse.success("Request approved and student updated");
                })
                .orElse(MessageResponse.error("Request not found or not pending"));
    }

    @Transactional
    public MessageResponse reject(Long id, String reason) {
        return requestRepository.findById(id)
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .map(req -> {
                    req.setStatus(RequestStatus.REJECTED);
                    req.setRejectionReason(reason);
                    requestRepository.save(req);
                    return MessageResponse.success("Request rejected");
                })
                .orElse(MessageResponse.error("Request not found or not pending"));
    }

    private void applyChanges(Students student, String json) {
        try {
            Map<String, Object> changes = objectMapper.readValue(json, new TypeReference<>() {});
            changes.forEach((field, value) -> {
                switch (field) {
                    case "firstName" -> student.setFirstName((String) value);
                    case "lastName" -> student.setLastName((String) value);
                    case "email" -> student.setEmail((String) value);
                    case "matricule" -> student.setMatricule((String) value);
                    // add more editable fields as needed
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Invalid change payload", e);
        }
    }
}
