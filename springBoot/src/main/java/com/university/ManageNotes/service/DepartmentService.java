package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.mapper.DepartmentMapper;
import com.university.ManageNotes.mapper.BaseMapper;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentService extends BaseCrudService<Department, Long, DepartmentRequest, DepartmentResponse> {
    
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                           DepartmentMapper mapper,
                           UserRepository userRepository,
                           SubjectRepository subjectRepository) {
        super(departmentRepository, (BaseMapper<Department, DepartmentRequest, DepartmentResponse>) mapper);
        this.departmentRepository = departmentRepository;
        this.departmentMapper = mapper;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public MessageResponse switchDepartment(Long userId, Long deptId) {
        return departmentRepository.findById(deptId)
                .filter(d -> userRepository.existsById(userId))
                .map(dept -> userRepository.findById(userId).map(user -> {
                    user.setDepartment(dept.getName());
                    userRepository.save(user);
                    return MessageResponse.success("Now viewing department " + dept.getName());
                }).orElse(MessageResponse.error("User not found")))
                .orElse(MessageResponse.error("Department not found"));
    }

    @Transactional
    public DepartmentResponse createDepartmentWithSubjects(DepartmentRequest request) {
        // Create department
        Department department = departmentMapper.toEntity(request);
        Department savedDept = departmentRepository.save(department);
        
        // Assign existing subjects to department if provided
        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            subjectRepository.findAllById(request.getSubjectIds())
                    .forEach(subject -> {
                subject.setDepartment(savedDept);
                subjectRepository.save(subject);
            });
        }
        
        return getDepartmentDetails(savedDept.getId());
    }

    public DepartmentResponse getDepartmentDetails(Long deptId) {
        return departmentRepository.findById(deptId)
                .map(dept -> {
                    DepartmentResponse response = departmentMapper.toResponse(dept);
                    response.setSubjects(subjectRepository.findByDepartmentId(deptId)
                        .stream()
                        .map(subject -> {
                            String teacherName = null;
                            if (subject.getIdTeacher() != null) {
                                teacherName = userRepository.findById(subject.getIdTeacher())
                                    .map(teacher -> teacher.getFirstName() + " " + teacher.getLastName())
                                    .orElse(null);
                            }
                            
                            return SubjectResponse.builder()
                                .id(subject.getId())
                                .name(subject.getName())
                                .code(subject.getCode())
                                .credits(subject.getCredits())
                                .description(subject.getDescription())
                                .active(subject.getActive())
                                .level(subject.getLevel())
                                .cycle(subject.getCycle())
                                .semesterId(subject.getSemester() != null ? subject.getSemester().getId() : null)
                                .semesterName(subject.getSemester() != null ? subject.getSemester().getName() : null)
                                .departmentId(subject.getDepartment().getId())
                                .departmentName(subject.getDepartment().getName())
                                .teacherId(subject.getIdTeacher())
                                .teacherName(teacherName)
                                .build();
                        })
                        .toList());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public List<DepartmentResponse> getAllDepartmentsSortedByDate() {
        return departmentRepository.findAll().stream()
                .sorted((d1, d2) -> d2.getCreatedDate().compareTo(d1.getCreatedDate()))
                .map(dept -> getDepartmentDetails(dept.getId()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public MessageResponse delete(Long id) {
        var subjects = subjectRepository.findByDepartmentId(id);
        subjects.forEach(subject -> subject.setDepartment(null));
        subjectRepository.saveAll(subjects);
        departmentRepository.deleteById(id);
        return MessageResponse.success("Department deleted successfully. " + subjects.size() + " subjects are now unassigned.");
    }
}
