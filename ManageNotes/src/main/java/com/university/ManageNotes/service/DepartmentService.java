package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.mapper.DepartmentMapper;
import com.university.ManageNotes.mapper.BaseMapper;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public DepartmentResponse getDepartmentDetails(Long deptId) {
        return departmentRepository.findById(deptId)
                .map(dept -> {
                    DepartmentResponse response = departmentMapper.toResponse(dept);
                    response.setSubjects(subjectRepository.findByDepartmentId(deptId)
                        .stream()
                        .map(subject -> {
                            var subjectResponse = new com.university.ManageNotes.dto.Response.SubjectResponse();
                            subjectResponse.setId(subject.getId());
                            subjectResponse.setName(subject.getName());
                            subjectResponse.setCode(subject.getCode());
                            subjectResponse.setDepartmentId(subject.getDepartment().getId());
                            subjectResponse.setDepartmentName(subject.getDepartment().getName());
                            return subjectResponse;
                        })
                        .toList());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
