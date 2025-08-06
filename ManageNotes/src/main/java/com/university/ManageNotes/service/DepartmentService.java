package com.university.ManageNotes.service;

// ... existing code ... <imports>
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
}
