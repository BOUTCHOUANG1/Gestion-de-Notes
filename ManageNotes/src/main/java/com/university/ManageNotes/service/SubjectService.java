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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService extends BaseCrudService<Subject, Long, SubjectRequest, SubjectResponse> {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper mapper;
    private final SemesterRepository semesterRepository;
    private final DepartmentRepository departmentRepository;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper mapper, SemesterRepository semesterRepository, DepartmentRepository departmentRepository) {
        super(subjectRepository, (BaseMapper<Subject, SubjectRequest, SubjectResponse>) mapper);
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
        this.semesterRepository = semesterRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAllOrderByName().stream().map(mapper::toResponse).toList();
    }

    @Override
    public MessageResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.getCode())) {
            return MessageResponse.error("Subject code already exists");
        }
        var subject = mapper.toEntity(request);
        subject.setLevel(request.getLevel());
        subject.setCycle(request.getCycle());
        subject.setSemester(semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new RuntimeException("Semester not found")));

        // Functional style: retrieve department via Optional and map
        subject.setDepartment(
                java.util.Optional.ofNullable(request.getDepartmentName())
                        .flatMap(departmentRepository::findByName)
                        .orElseGet(() -> {
                            var dept = com.university.ManageNotes.model.Department.builder()
                                    .name(request.getDepartmentName())
                                    .build();
                            return departmentRepository.save(dept);
                        })
        );

        subjectRepository.save(subject);
        return com.university.ManageNotes.dto.Response.MessageResponse.success("Subject created");
    }

    @Override
    public MessageResponse update(Long id, SubjectRequest request) {
        return subjectRepository.findById(id)
                .map(existing -> {
                    if (!existing.getCode().equals(request.getCode()) && subjectRepository.existsByCode(request.getCode())) {
                        return MessageResponse.error("Subject code already exists");
                    }
                    existing.setName(request.getName());
                    existing.setCode(request.getCode());
                    existing.setDescription(request.getDescription());
                    existing.setCredits(java.math.BigDecimal.valueOf(request.getCredits()));
                    existing.setIdTeacher(request.getTeacherId());
                    existing.setActive(request.getActive());
                    existing.setLevel(request.getLevel());
                    existing.setCycle(request.getCycle());
                    existing.setSemester(semesterRepository.findById(request.getSemesterId())
                            .orElseThrow(() -> new RuntimeException("Semester not found")));
                    existing.setDepartment(
                            java.util.Optional.ofNullable(request.getDepartmentName())
                                    .flatMap(departmentRepository::findByName)
                                    .orElseGet(() -> {
                                        var dept = com.university.ManageNotes.model.Department.builder()
                                                .name(request.getDepartmentName())
                                                .build();
                                        return departmentRepository.save(dept);
                                    })
                    );
                    subjectRepository.save(existing);
                    return MessageResponse.success("Subject updated");
                })
                .orElse(MessageResponse.error("Subject not found"));
    }

    public List<SubjectResponse> getSubjectsByTeacher(Long teacherId) {
        return subjectRepository.findSubjectsByTeacherOrderByName(teacherId).stream().map(mapper::toResponse).toList();
    }

    public List<SubjectResponse> getSubjectsByDepartment(Long deptId) {
        return subjectRepository.findByDepartmentId(deptId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
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
        // Functional-style: use Optional to branch without imperative if/else.
        return subjectRepository.findById(id)
                .map(entity -> {
                    subjectRepository.delete(entity);
                    return com.university.ManageNotes.dto.Response.MessageResponse.success("Deleted successfully");
                })
                .orElseGet(() -> com.university.ManageNotes.dto.Response.MessageResponse.error("Subject not found"));
    }
}
