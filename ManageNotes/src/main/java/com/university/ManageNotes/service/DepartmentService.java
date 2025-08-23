package com.university.ManageNotes.service;

// ... existing code ... <imports>
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final SubjectService subjectService;

    public List<Department> list() {
        return departmentRepository.findAll();
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
                    DepartmentResponse response = new DepartmentResponse();
                    response.setId(dept.getId());
                    response.setName(dept.getName());
                    response.setSubjects(subjectService.getSubjectsByDepartment(dept.getId()));
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }
}
