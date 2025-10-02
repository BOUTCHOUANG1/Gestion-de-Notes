package com.university.ManageNotes.config;

import com.university.ManageNotes.dto.Response.GradeResponse;
import com.university.ManageNotes.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STANDARD)
            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true);
        
        mapper.addConverter(context -> context.getSource() != null ? 
            ((java.math.BigDecimal) context.getSource()).doubleValue() : null,
            java.math.BigDecimal.class, Double.class);
        
        // Explicit mapping for Grades to GradeResponse
        mapper.addMappings(new PropertyMap<Grades, GradeResponse>() {
            @Override
            protected void configure() {
                map().setGradeId(source.getGradeId());
                map().setCcScore(source.getCcScore());
                map().setSnScore(source.getSnScore());
                map().setTotalScore(source.getTotalScore());
                map().setComments(source.getComments());
                map().setHasPassed(source.getHasPassed());
                map().setGpa(source.getGpa());
                map().setCreatedDate(source.getCreatedDate());
                map().setLastModifiedDate(source.getLastModifiedDate());
            }
        });
        
        // Mapping for Student to SimpleStudentResponse
        mapper.addMappings(new PropertyMap<Student, GradeResponse.SimpleStudentResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setEmail(source.getEmail());
                map().setMatricule(source.getMatricule());
            }
        });
        
        // Mapping for Subject to SimpleSubjectResponse
        mapper.addMappings(new PropertyMap<Subject, GradeResponse.SimpleSubjectResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getSubjectId());
                map().setSubjectName(source.getSubjectName());
                map().setSubjectCode(source.getSubjectCode());
            }
        });
        
        // Mapping for Teacher to SimpleTeacherResponse
        mapper.addMappings(new PropertyMap<Teacher, GradeResponse.SimpleTeacherResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setUsername(source.getUsername());
                map().setFirstName(source.getFirstName());
                map().setLastName(source.getLastName());
                map().setEmail(source.getEmail());
            }
        });
        
        // Mapping for Semester to SimpleSemesterResponse
        mapper.addMappings(new PropertyMap<Semester, GradeResponse.SimpleSemesterResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getSemesterId());
                map().setName(source.getName());
                map().setActive(source.getActive());
            }
        });
        
        return mapper;
    }
}
