package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.mapper.SubjectMapper;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService extends BaseCrudService<Subject, Long, SubjectRequest, SubjectResponse> {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper mapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper mapper) {
        super(subjectRepository, mapper);
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
    }

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAllOrderByName().stream().map(mapper::toResponse).toList();
    }

    @Override
    public MessageResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            return MessageResponse.error("Subject code already exists");
        }
        return super.create(request);
    }

    @Override
    public MessageResponse update(Long id, SubjectRequest request) {
        Subject existing = subjectRepository.findById(id).orElseThrow(() -> new RuntimeException("Subject not found"));
        if (!existing.getCode().equals(request.getCode()) && subjectRepository.existsByCode(request.getCode())) {
            return MessageResponse.error("Subject code already exists");
        }
        return super.update(id, request);
    }

    public List<SubjectResponse> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findSubjectsByTeacherOrderByName(teacherId).stream().map(mapper::toResponse).toList();
    }

    public List<SubjectResponse> searchSubjects(String term) {
        return subjectRepository.findByNameContainingIgnoreCase(term).stream().map(mapper::toResponse).toList();
    }

    // Adapter methods for existing controllers
    public SubjectResponse getSubjectById(Long id) {
        return super.getById(id);
    }

    public MessageResponse createSubject(SubjectRequest request) {
        return create(request);
    }

    public MessageResponse updateSubject(Long id, SubjectRequest request) {
        return update(id, request);
    }

    public MessageResponse deleteSubject(Long id) {
        return delete(id);
    }
}
