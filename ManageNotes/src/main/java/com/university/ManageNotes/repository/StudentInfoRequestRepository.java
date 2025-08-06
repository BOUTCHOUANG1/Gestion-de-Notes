package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.RequestStatus;
import com.university.ManageNotes.model.StudentInfoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInfoRequestRepository extends JpaRepository<StudentInfoRequest, Long> {
    List<StudentInfoRequest> findByStatus(RequestStatus status);
}
