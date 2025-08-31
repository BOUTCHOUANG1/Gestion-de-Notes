package com.university.ManageNotes;

import com.university.ManageNotes.dto.Request.SubjectRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.StudentCycle;
import com.university.ManageNotes.model.StudentLevel;
import com.university.ManageNotes.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SubjectServiceConstraintTest {

    @Autowired
    private SubjectService subjectService;

    @Test
    void shouldPreventTeacherFromTeachingMultipleSubjectsAtSameLevel() {
        // Given: First subject for teacher at LEVEL1
        SubjectRequest firstSubject = createSubjectRequest("MATH101", "Mathematics 101", 1L, StudentLevel.LEVEL1);
        MessageResponse firstResponse = subjectService.create(firstSubject);
        assertThat(firstResponse.getStatus()).isEqualTo("success");

        // When: Trying to assign same teacher to another subject at LEVEL1
        SubjectRequest secondSubject = createSubjectRequest("PHYS101", "Physics 101", 1L, StudentLevel.LEVEL1);
        MessageResponse secondResponse = subjectService.create(secondSubject);

        // Then: Should fail with appropriate error message
        assertThat(secondResponse.getStatus()).isEqualTo("error");
        assertThat(secondResponse.getMessage()).contains("Teacher is already assigned to a subject at this level");
    }

    @Test
    void shouldAllowTeacherToTeachDifferentLevels() {
        // Given: First subject for teacher at LEVEL1
        SubjectRequest level1Subject = createSubjectRequest("MATH101", "Mathematics 101", 1L, StudentLevel.LEVEL1);
        MessageResponse firstResponse = subjectService.create(level1Subject);
        assertThat(firstResponse.getStatus()).isEqualTo("success");

        // When: Assigning same teacher to subject at LEVEL2
        SubjectRequest level2Subject = createSubjectRequest("MATH201", "Mathematics 201", 1L, StudentLevel.LEVEL2);
        MessageResponse secondResponse = subjectService.create(level2Subject);

        // Then: Should succeed
        assertThat(secondResponse.getStatus()).isEqualTo("success");
    }

    @Test
    void shouldAllowDifferentTeachersAtSameLevel() {
        // Given: First teacher at LEVEL1
        SubjectRequest teacher1Subject = createSubjectRequest("MATH101", "Mathematics 101", 1L, StudentLevel.LEVEL1);
        MessageResponse firstResponse = subjectService.create(teacher1Subject);
        assertThat(firstResponse.getStatus()).isEqualTo("success");

        // When: Different teacher at same level
        SubjectRequest teacher2Subject = createSubjectRequest("PHYS101", "Physics 101", 2L, StudentLevel.LEVEL1);
        MessageResponse secondResponse = subjectService.create(teacher2Subject);

        // Then: Should succeed
        assertThat(secondResponse.getStatus()).isEqualTo("success");
    }

    private SubjectRequest createSubjectRequest(String code, String name, Long teacherId, StudentLevel level) {
        SubjectRequest request = new SubjectRequest();
        request.setCode(code);
        request.setName(name);
        request.setTeacherId(teacherId);
        request.setLevel(level);
        request.setCycle(StudentCycle.BACHELOR);
        request.setCredits(3);
        request.setDepartmentId(1L);  // Assuming department with ID 1 exists
        request.setSemesterId(1L);    // Assuming semester with ID 1 exists
        request.setActive(true);
        request.setDescription("Test subject description");
        return request;
    }
}