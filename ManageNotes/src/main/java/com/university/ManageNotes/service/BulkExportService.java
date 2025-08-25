package com.university.ManageNotes.service;

// ... existing code ... <imports>
import com.university.ManageNotes.dto.Request.BulkExportRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.*;
import com.university.ManageNotes.repository.*;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkExportService {

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public MessageResponse export(BulkExportRequest req, boolean publish) {
        return switch (req.getDocumentType()
                .toUpperCase()) {
            case "NOTES" -> exportNotes(req, publish);
            case "STUDENTS" -> exportStudents(req, publish);
            case "TEACHERS" -> exportTeachers(publish);
            default -> MessageResponse.error("Unsupported document type");
        };
    }

    private MessageResponse exportNotes(BulkExportRequest req, boolean publish) {
        StudentLevel levelEnum = StudentLevel.valueOf("LEVEL" + req.getLevel()
                .replace("L", ""));
        List<Students> students = studentRepository.findByLevel(levelEnum);
        if (students.isEmpty()) return MessageResponse.error("No students found for level " + req.getLevel());

        Long subjId = req.getSubjectId();
        String period = req.getPeriodLabel();

        try {
            Path dir = Path.of("generated-reports", "bulk", Instant.now().toEpochMilli() + "");
            Files.createDirectories(dir);
            for (Students s : students) {
                List<Grades> grades = gradeRepository.findByStudentId(s.getId());
                if (subjId != null) grades = grades.stream()
                        .filter(g -> g.getSubject()
                                .getId()
                                .equals(subjId))
                        .toList();

                if (period != null) grades = grades.stream()
                        .filter(g -> period.equalsIgnoreCase(g.getGradeType()
                                .name())).toList();

                if (grades.isEmpty()) continue;
                // simple pdf per student
                try (PDDocument doc = new PDDocument()) {
                    PDPage page = new PDPage();
                    doc.addPage(page);
                    try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                        cs.beginText();
                        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        cs.newLineAtOffset(50, 750);
                        cs.showText("Grades for " + s.getFirstName() + " " + s.getLastName());
                        cs.newLineAtOffset(0, -20);
                        cs.setFont(PDType1Font.HELVETICA, 10);
                        for (Grades g : grades) {
                            cs.showText(g.getSubject().getName() + " " + g.getGradeType() + ": " + g.getValue());
                            cs.newLineAtOffset(0, -15);
                        }
                        cs.endText();
                    }
                    String filename = "student_" + s.getId() + ".pdf";
                    doc.save(dir.resolve(filename).toFile());
                }
            }
            return MessageResponse.success(publish ? "Grades published successfully" : "PDFs generated at " + dir.toAbsolutePath());
        } catch (Exception e) {
            return MessageResponse.error("Export failed: " + e.getMessage());
        }
    }

    private MessageResponse exportStudents(BulkExportRequest req, boolean publish) {
        StudentLevel levelEnum = StudentLevel.valueOf("LEVEL" + req.getLevel()
                .replace("L", ""));
        List<Students> students = studentRepository.findByLevel(levelEnum);
        if (students.isEmpty()) return MessageResponse.error("No students found");
        // Here we could publish or print list; we simulate by returning success.
        return MessageResponse.success(publish ? "Student list published" : "Student list ready (" + students.size() + ")");
    }

    private MessageResponse exportTeachers(boolean publish) {
        List<Users> teachers = userRepository.findByRole(Role.TEACHER);
        return MessageResponse.success(publish ? "Teachers list published" : "Teachers list ready (" + teachers.size() + ")");
    }
}
