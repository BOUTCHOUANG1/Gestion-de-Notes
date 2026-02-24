package com.university.ManageNotes.service;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.SubjectResponse;

import java.util.List;


public interface SubjectService {

    List<SubjectResponse> getAllSubjects(Integer pageNumber,
                                   Integer pageSize,
                                   String sortBy, String sortOrder);

    SubjectResponse createSubject(SubjectRequest request);

    SubjectResponse updateSubject(Long id, SubjectRequest request);

    SubjectResponse deleteSubject(Long id);

    List<SubjectResponse> getAllSubjectsByTeacher(Long teacherId,
                                            Integer pageNumber,
                                            Integer pageSize,
                                            String sortBy, String sortOrder);
}
