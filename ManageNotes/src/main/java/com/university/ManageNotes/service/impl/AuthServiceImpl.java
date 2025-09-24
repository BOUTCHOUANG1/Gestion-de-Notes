package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.dto.Response.UserResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public MessageResponse changePassword(String username, String newPassword) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new APIException("Error: User is not found."));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(false);
        userRepository.save(user);

        return new MessageResponse("Password changed successfully!");
    }

    @Override
    public UserResponse getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        UserResponse userResponse = new UserResponse();

        if (roles.contains(AppRole.ROLE_TEACHER)) {
            Teacher teacher = teacherRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", "teacherId", userDetails.getId()));

            List<Subject> subject = subjectRepository.findSubjectByTeacher_Email(userDetails.getEmail());

            Department department = departmentRepository.findDepartmentByTeacher_Id(userDetails.getId());

            if(subject.isEmpty()) {
                throw new APIException("No subjects were found for this teacher with name " + userDetails.getUsername());
            }

            getAdminResponse(teacher);
            userResponse.setPhone(teacher.getPhoneNumber());
            userResponse.setDepartment(department);
            userResponse.setLevels(teacher.getTeachingLevel());
            userResponse.setSubjects(subject);
            Users userEntity = modelMapper.map(userResponse, Users.class);
            userRepository.save(userEntity);
            return userResponse;

        } else if (roles.contains(AppRole.ROLE_STUDENT)) {
            Student student = studentRepository.findByMatricule(userDetails.getUsername())
                    .orElseThrow(() -> new APIException("Student record not found"));

            Users userEntity = modelMapper.map(userResponse, Users.class);
            userRepository.save(userEntity);
            return getUserResponse(student);
        }

        else if (roles.contains(AppRole.ROLE_ADMIN)) {
            Users admin = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Admin", "adminId", userDetails.getId()));

            Users userEntity = modelMapper.map(userResponse, Users.class);
            userRepository.save(userEntity);
           return getAdminResponse(admin);
        } else {
            throw new APIException("User role not recognized");
        }
    }

    private static UserResponse getAdminResponse(Users admin) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(admin.getUsername());
        userResponse.setFirstName(admin.getFirstName());
        userResponse.setLastName(admin.getLastName());
        userResponse.setEmail(admin.getEmail());
        userResponse.setAppRole((Roles) admin.getRoles());
        userResponse.setIsActive(admin.getIsActive());
        return userResponse;
    }

    private static UserResponse getUserResponse(Student student) {
        UserResponse userResponse = new UserResponse();
        userResponse.setFirstName(student.getFirstName());
        userResponse.setLastName(student.getLastName());
        userResponse.setMatricule(student.getMatricule());
        userResponse.setLevel(student.getStudentLevel());
        userResponse.setCycle(student.getCycle());
        userResponse.setSpeciality(student.getSpeciality());
        userResponse.setDateOfBirth(student.getDateOfBirth());
        userResponse.setEmail(student.getEmail());
        userResponse.setAppRole((Roles) student.getRoles());
        userResponse.setIsActive(student.getIsActive());
        return userResponse;
    }
}
