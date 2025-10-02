package com.university.ManageNotes.util;

import com.university.ManageNotes.dto.Response.*;
import com.university.ManageNotes.model.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    public SubjectResponse toSubjectResponse(Subject subject) {
        if (subject == null) return null;
        
        SubjectResponse response = new SubjectResponse();
        response.setSubjectId(subject.getSubjectId());
        response.setSubjectName(subject.getSubjectName());
        response.setSubjectCode(subject.getSubjectCode());
        response.setCredits(subject.getCredits());
        response.setDescription(subject.getDescription());
        response.setStudentCycle(subject.getStudentcycle());
        response.setDepartmentId(subject.getDepartment() != null ? subject.getDepartment().getDepartmentId() : null);
        
        if (subject.getSubjectLevel() != null) {
            response.setSubjectsLevel(java.util.Collections.singleton(subject.getSubjectLevel()));
        }
        
        if (subject.getTeacher() != null) {
            response.setTeacher(toTeacherResponseBasic(subject.getTeacher()));
        }
        
        return response;
    }

    public TeacherResponse toTeacherResponseBasic(Teacher teacher) {
        if (teacher == null) return null;
        
        TeacherResponse response = new TeacherResponse();
        response.setTeacherId(teacher.getId());
        response.setUsername(teacher.getUsername());
        response.setFirstName(teacher.getFirstName());
        response.setLastName(teacher.getLastName());
        response.setEmail(teacher.getEmail());
        response.setPhoneNumber(teacher.getPhoneNumber());
        response.setCreatedDate(teacher.getCreatedDate());
        response.setLastModifiedDate(teacher.getLastModifiedDate());
        response.setIsActive(teacher.getIsActive());
        response.setRole(teacher.getRole() != null ? teacher.getRole().getAppRole().name() : null);
        
        if (teacher.getTeachingLevels() != null) {
            response.setTeachingLevel(teacher.getTeachingLevels().stream()
                .collect(Collectors.toSet()));
        }
        
        return response;
    }

    public DepartmentResponse toDepartmentResponse(Department department) {
        if (department == null) return null;
        
        DepartmentResponse response = new DepartmentResponse();
        response.setDepartmentId(department.getDepartmentId());
        response.setDepartmentName(department.getDepartmentName());
        response.setCreatedDate(department.getCreatedDate());
        response.setLastModifiedDate(department.getLastModifiedDate());
        
        try {
            Set<Subject> subjects = department.getSubjects();
            System.out.println("Department " + department.getDepartmentId() + " has " + (subjects != null ? subjects.size() : "null") + " subjects");
            if (subjects != null && !subjects.isEmpty()) {
                Set<SubjectResponse> subjectResponses = subjects.stream()
                    .map(this::toSubjectResponse)
                    .collect(Collectors.toSet());
                response.setDepartmentSubjects(subjectResponses);
                System.out.println("Mapped " + subjectResponses.size() + " subject responses");
            }
        } catch (Exception e) {
            System.err.println("Error loading department subjects: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
}
