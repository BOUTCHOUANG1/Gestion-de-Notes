package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.mapper.BaseMapper;
import com.university.ManageNotes.mapper.SubjectMapper;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.SemesterRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService extends BaseCrudService<Subject, Long, SubjectRequest, SubjectResponse> {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper mapper;
    private final SemesterRepository semesterRepository;
    private final DepartmentRepository departmentRepository;
    private final com.university.ManageNotes.repository.UserRepository userRepository;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper mapper, SemesterRepository semesterRepository, DepartmentRepository departmentRepository, com.university.ManageNotes.repository.UserRepository userRepository) {
        super(subjectRepository, (BaseMapper<Subject, SubjectRequest, SubjectResponse>) mapper);
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
        this.semesterRepository = semesterRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .sorted((s1, s2) -> s2.getCreatedDate().compareTo(s1.getCreatedDate()))
                .map(this::enrichSubjectResponse)
                .toList();
    }

    @Override
    public MessageResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            return MessageResponse.error("Subject code already exists");
        }
        
        // Check if teacher is already assigned to a subject at this level
        if (request.getTeacherId() != null && 
            subjectRepository.existsByIdTeacherAndLevel(request.getTeacherId(), request.getLevel())) {
            return MessageResponse.error("Teacher is already assigned to a subject at this level");
        }
        
        var subject = mapper.toEntity(request);
        subject.setLevel(request.getLevel());
        subject.setCycle(request.getCycle());
        subject.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));

        // Set department using departmentId
        subject.setDepartment(departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));

        subjectRepository.save(subject);
        return MessageResponse.success("Subject created");
    }

    @Override
    public MessageResponse update(Long id, SubjectRequest request) {
        return subjectRepository.findById(id)
                .map(existing -> {
                    if (!existing.getCode().equals(request.getCode()) && subjectRepository.existsByCode(request.getCode())) {
                        return MessageResponse.error("Subject code already exists");
                    }
                    
                    // Check if teacher is already assigned to another subject at this level
                    if (request.getTeacherId() != null && 
                        (!request.getTeacherId().equals(existing.getIdTeacher()) || !request.getLevel().equals(existing.getLevel()))) {
                        if (subjectRepository.existsByIdTeacherAndLevel(request.getTeacherId(), request.getLevel())) {
                            return MessageResponse.error("Teacher is already assigned to a subject at this level");
                        }
                    }
                    
                    existing.setName(request.getName());
                    existing.setCode(request.getCode());
                    existing.setDescription(request.getDescription());
                    existing.setCredits(BigDecimal.valueOf(request.getCredits()));
                    existing.setIdTeacher(request.getTeacherId());
                    existing.setActive(request.getActive());
                    existing.setLevel(request.getLevel());
                    existing.setCycle(request.getCycle());
                    existing.setSemester(semesterRepository.findById(request.getSemesterId())
                            .orElseThrow(() -> new RuntimeException("Semester not found")));
                    existing.setDepartment(departmentRepository.findById(request.getDepartmentId())
                            .orElseThrow(() -> new RuntimeException("Department not found")));
                    subjectRepository.save(existing);
                    return MessageResponse.success("Subject updated");
                })
                .orElse(MessageResponse.error("Subject not found"));
    }

    public List<SubjectResponse> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findSubjectsByTeacherOrderByName(teacherId).stream()
                .map(this::enrichSubjectResponse)
                .toList();
    }



    // Adapter methods for existing controllers
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return enrichSubjectResponse(subject);
    }
    
    private SubjectResponse enrichSubjectResponse(Subject subject) {
        SubjectResponse response = mapper.toResponse(subject);
        if (subject.getIdTeacher() != null) {
            userRepository.findById(subject.getIdTeacher())
                    .ifPresent(teacher -> response.setTeacherName(
                            teacher.getFirstName() + " " + teacher.getLastName()));
        }
        return response;
    }

    public MessageResponse createSubject(SubjectRequest request) {
        return create(request);
    }

    public MessageResponse updateSubject(Long id, SubjectRequest request) {
        return update(id, request);
    }

    public MessageResponse deleteSubject(Long id) {
        // Functional-style: use Optional to branch without imperative if/else.
        return subjectRepository.findById(id)
                .map(entity -> {
                    subjectRepository.delete(entity);
                    return MessageResponse.success("Deleted successfully");
                })
                .orElseGet(() -> MessageResponse.error("Subject not found"));
    }
}
