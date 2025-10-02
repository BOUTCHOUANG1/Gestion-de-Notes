package com.university.ManageNotes.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        
        // Set matching strategy to STRICT to avoid unintended mappings
        mapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true)
            .setAmbiguityIgnored(true)
            .setImplicitMappingEnabled(false);
        
        // Configure BigDecimal to Double conversion
        mapper.addConverter(context -> context.getSource() != null ? 
            ((java.math.BigDecimal) context.getSource()).doubleValue() : null,
            java.math.BigDecimal.class, Double.class);
        
        return mapper;
    }
}
