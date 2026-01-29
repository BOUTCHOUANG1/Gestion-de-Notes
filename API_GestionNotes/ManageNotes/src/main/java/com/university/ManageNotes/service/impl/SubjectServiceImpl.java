package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.dto.Response.TeacherResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.model.Teacher;
import com.university.ManageNotes.repository.*;
import com.university.ManageNotes.service.SubjectService;
import com.university.ManageNotes.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import com.university.ManageNotes.mapper.SubjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final TeachingLevelRepository teachingLevelRepository;
    private final DepartmentRepository departmentRepository;
    private final SemesterRepository semesterRepository;
    private final SubjectMapper subjectMapper;
    private final ResponseMapper responseMapper;

    @Override
    public List<SubjectResponse> getAllSubjects(Integer pageNumber,
                                   Integer pageSize,
                                   String sortBy, String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page subjectPage = this.subjectRepository.findAll(pageable);

        List<Subject> subjectList = subjectPage.getContent();

        if(subjectList.isEmpty()){
            throw new APIException("No subjects found");
        }

        return subjectList.stream()
                .map(responseMapper::toSubjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectResponse> getAllSubjectsByTeacher(Long teacherId,
                                                         Integer pageNumber,
                                                         Integer pageSize,
                                                         String sortBy, String sortOrder){
        Teacher teacherDb = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));

        List<Subject> subjectByTeacherDb = subjectRepository.findSubjectByTeacher_Email(teacherDb.getEmail());

        if(subjectByTeacherDb.isEmpty()){
            throw new APIException("No Subjects were found for this teacher with email " + teacherDb.getFirstName());
        }

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page subjectPage = this.subjectRepository.findAll(pageable);

        List<Subject> subjectList = subjectPage.getContent();

        if(subjectList.isEmpty()){
            throw new APIException("No subjects found");
        }

        return subjectList.stream()
                .map(responseMapper::toSubjectResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest request) {
         if(this.subjectRepository.findBySubjectCode(request.getSubjectCode()).isPresent()) {
             throw new APIException("Subject with code " + request.getSubjectCode() + " already exists");
         }

         Subject subject = new Subject();
         subject.setSubjectName(request.getSubjectName());
         subject.setSubjectCode(request.getSubjectCode());
         subject.setCredits(request.getCredits());
         subject.setDescription(request.getDescription());
         subject.setStudentcycle(request.getStudentcycle());
         
         if (request.getTeacherId() != null) {
             Teacher teacher = teacherRepository.findById(request.getTeacherId())
                     .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));
             subject.setTeacher(teacher);
         }
         
         if (request.getSubjectsLevel() != null && !request.getSubjectsLevel().isEmpty()) {
             String levelName = request.getSubjectsLevel().get(0);
             com.university.ManageNotes.model.enums.StudentLevel studentLevel = com.university.ManageNotes.model.enums.StudentLevel.valueOf(levelName);
             com.university.ManageNotes.model.TeachingLevel level = teachingLevelRepository.findAll().stream()
                     .filter(tl -> tl.getStudentLevel() == studentLevel)
                     .findFirst()
                     .orElseThrow(() -> new APIException("Teaching level " + levelName + " not found"));
             subject.setSubjectLevel(level);
         }
         
         if (request.getDepartmentId() != null) {
             com.university.ManageNotes.model.Department department = departmentRepository.findById(request.getDepartmentId())
                     .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
             subject.setDepartment(department);
         }
         
         if (request.getSemesterId() != null) {
             com.university.ManageNotes.model.Semester semester = semesterRepository.findById(request.getSemesterId())
                     .orElseThrow(() -> new ResourceNotFoundException("Semester", "id", request.getSemesterId()));
             subject.setSemester(semester);
         }
         
         Subject saved = subjectRepository.save(subject);
         return subjectMapper.toSubjectResponse(saved);
     }

    @Override
    @Transactional
    public SubjectResponse updateSubject(Long subjectId, SubjectRequest request) {
         Subject subjectFromDb = this.subjectRepository.findById(subjectId)
                 .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", subjectId));

         Subject subject = subjectMapper.toEntity(request);

         if (subject.getSubjectName() != null) {
             subjectFromDb.setSubjectName(subject.getSubjectName());
         }
         if (subject.getSubjectCode() != null) {
             subjectFromDb.setSubjectCode(subject.getSubjectCode());
         }
         if (subject.getCredits() != null) {
             subjectFromDb.setCredits(subject.getCredits());
         }
         if (subject.getDescription() != null) {
             subjectFromDb.setDescription(subject.getDescription());
         }
         if (subject.getSubjectLevel() != null) {
             subjectFromDb.setSubjectLevel(subject.getSubjectLevel());
         }
         if (subject.getStudentcycle() != null) {
             subjectFromDb.setStudentcycle(subject.getStudentcycle());
         }
         if (subject.getTeacher() != null) {
             subjectFromDb.setTeacher(subject.getTeacher());
         }
         if (subject.getSemester() != null) {
             subjectFromDb.setSemester(subject.getSemester());
         }
         if (subject.getDepartment() != null) {
             subjectFromDb.setDepartment(subject.getDepartment());
         }

         return subjectMapper.toSubjectResponse(subjectRepository.save(subjectFromDb));
     }

    @Override
    @Transactional
    public SubjectResponse deleteSubject(Long id) {
         Subject subjectFoundDb = this.subjectRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", id));
         
         SubjectResponse response = subjectMapper.toSubjectResponse(subjectFoundDb);
         this.subjectRepository.deleteSubjectById(id);
         return response;
     }
}
