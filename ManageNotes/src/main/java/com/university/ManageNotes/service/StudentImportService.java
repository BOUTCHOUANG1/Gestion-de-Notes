package com.university.ManageNotes.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.university.ManageNotes.dto.Request.SignupRequest;
import com.university.ManageNotes.dto.Response.MessageResponse;
import com.university.ManageNotes.model.Role;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentImportService {

    @Autowired
    private AuthService authService;

    @Transactional
    public List<MessageResponse> importFromExcel(MultipartFile file) throws Exception {
        List<MessageResponse> responses = new ArrayList<>();
        try (InputStream is = file.getInputStream(); XSSFWorkbook wb = new XSSFWorkbook(is)) {
            var sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                if (r == null) continue;
                SignupRequest req = mapRow(r);
                responses.add(authService.registerUser(req));
            }
        }
        return responses;
    }

    private SignupRequest mapRow(Row r) {
        SignupRequest req = new SignupRequest();
        req.setUsername(r.getCell(0).getStringCellValue());
        req.setEmail(r.getCell(1).getStringCellValue());
        req.setPassword(r.getCell(2).getStringCellValue());
        req.setFirstName(r.getCell(3).getStringCellValue());
        req.setLastName(r.getCell(4).getStringCellValue());
        req.setMatricule(r.getCell(5).getStringCellValue());
        req.setLevel(com.university.ManageNotes.model.StudentLevel.valueOf("LEVEL" + (int) r.getCell(6).getNumericCellValue()));
        req.setCycle(com.university.ManageNotes.model.StudentCycle.valueOf(r.getCell(7).getStringCellValue().toUpperCase()));
        req.setSpeciality(r.getCell(8).getStringCellValue());
        req.setDateOfBirth(r.getCell(9) != null ? r.getCell(9).getLocalDateTimeCellValue().toLocalDate() : null);
        req.setPlaceOfBirth(r.getCell(10) != null ? r.getCell(10).getStringCellValue() : null);
        req.setRole(Role.STUDENT);
        return req;
    }

    @Transactional
    public List<MessageResponse> importFromCsv(MultipartFile file) throws Exception {
        List<MessageResponse> responses = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] row;
            reader.readNext(); // skip header
            while ((row = reader.readNext()) != null) {
                SignupRequest req = new SignupRequest();
                req.setUsername(row[0]);
                req.setEmail(row[1]);
                req.setPassword(row[2]);
                req.setFirstName(row[3]);
                req.setLastName(row[4]);
                req.setMatricule(row[5]);
                req.setLevel(com.university.ManageNotes.model.StudentLevel.valueOf("LEVEL" + row[6]));
                req.setCycle(com.university.ManageNotes.model.StudentCycle.valueOf(row[7].toUpperCase()));
                req.setSpeciality(row[8]);
                req.setDateOfBirth(LocalDate.parse(row[9]));
                req.setPlaceOfBirth(row[10]);
                req.setRole(Role.STUDENT);
                responses.add(authService.registerUser(req));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return responses;
    }

    @Transactional
    public List<MessageResponse> importFromExternalDb(String jdbcUrl, String username, String password, String query) throws Exception {
        List<MessageResponse> responses = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SignupRequest req = new SignupRequest();
                req.setUsername(rs.getString("username"));
                req.setEmail(rs.getString("email"));
                req.setPassword(rs.getString("password") != null ? rs.getString("password") : "password");
                req.setFirstName(rs.getString("first_name"));
                req.setLastName(rs.getString("last_name"));
                req.setMatricule(rs.getString("matricule"));
                req.setLevel(com.university.ManageNotes.model.StudentLevel.valueOf("LEVEL" + rs.getInt("level")));
                req.setCycle(com.university.ManageNotes.model.StudentCycle.valueOf(rs.getString("cycle").toUpperCase()));
                req.setSpeciality(rs.getString("speciality"));
                req.setDateOfBirth(rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null);
                req.setPlaceOfBirth(rs.getString("place_of_birth"));
                req.setRole(Role.STUDENT);
                responses.add(authService.registerUser(req));
            }
        }
        return responses;
    }
}
