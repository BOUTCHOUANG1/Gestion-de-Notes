package com.university.ManageNotes.service.impl;

import com.university.ManageNotes.dto.Request.DepartmentRequest;
import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.DepartmentResponse;
import com.university.ManageNotes.dto.Response.SubjectResponse;
import com.university.ManageNotes.exception.APIException;
import com.university.ManageNotes.exception.ResourceNotFoundException;
import com.university.ManageNotes.model.Department;
import com.university.ManageNotes.model.Subject;
import com.university.ManageNotes.repository.DepartmentRepository;
import com.university.ManageNotes.repository.SubjectRepository;
import com.university.ManageNotes.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;

    @Override
    public DepartmentRequest createDepartment(DepartmentRequest request) {
       Department department = modelMapper.map(request, Department.class);

       Department departmentDb= this.departmentRepository.findByDepartmentName(department.getDepartmentName());

       if(departmentDb != null){
           throw new APIException("Department with name " + department.getDepartmentName() + " already exists !!!");
       }

        return modelMapper.map(this.departmentRepository.save(department), DepartmentRequest.class);
    }

    @Override
    public DepartmentResponse getAllDepartments(Integer pageNumber,
                                                      Integer pageSize,
                                                      String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page departmentPage = this.departmentRepository.findAll(pageable);

        List<Department> departments = departmentPage.getContent();

        if(departments.isEmpty()){
            throw new APIException("No departments found");
        }

        List<DepartmentRequest> departmentRequests = departments.stream()
                .map(dep -> modelMapper.map(dep, DepartmentRequest.class))
                .toList();

        List<Subject> subjects = this.subjectRepository.findAll();

        List<SubjectRequest> subjectRequests = subjects.stream()
                .map(sub -> modelMapper.map(sub, SubjectRequest.class))
                .toList();

        Set<Subject> subjectResponses = subjectRequests.stream()
                .map(sub -> modelMapper.map(sub, Subject.class))
                .collect(Collectors.toSet());

        DepartmentResponse departmentResponse = new DepartmentResponse();

        departmentResponse.setContent(departmentRequests);
        departmentResponse.setPageNumber(departmentPage.getNumber());
        departmentResponse.setPageSize(departmentPage.getSize());
        departmentResponse.setTotalElements(departmentPage.getTotalElements());
        departmentResponse.setTotalPages(departmentPage.getTotalPages());
        departmentResponse.setLastPage(departmentPage.isLast());
        departmentResponse.setSubjects(subjectResponses);
        return departmentResponse;
    }

    @Override
    public DepartmentRequest updateDepartment(DepartmentRequest departmentRequest, Long departmentId){
        Department department = modelMapper.map(departmentRequest, Department.class);

        Department departmentDb = this.departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "departmentId", departmentId));

        departmentDb.setDepartmentName(department.getDepartmentName());
        departmentDb.setSubjects(department.getSubjects());
        return modelMapper.map(this.departmentRepository.save(departmentDb), DepartmentRequest.class);
    }

    @Override
    public DepartmentRequest deleteDepartment(Long departmentId) {
        Department departmentToDelete = this.departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "departmentId", departmentId));

        this.departmentRepository.delete(departmentToDelete);
        return modelMapper.map(departmentToDelete, DepartmentRequest.class);
    }
}
