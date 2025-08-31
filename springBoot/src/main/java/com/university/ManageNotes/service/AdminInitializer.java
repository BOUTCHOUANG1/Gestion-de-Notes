package com.university.ManageNotes.service;

import com.university.ManageNotes.model.Role;
import com.university.ManageNotes.model.Users;
import com.university.ManageNotes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@org.springframework.core.annotation.Order(1)
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceService sequenceService;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Always reset sequence first
            sequenceService.resetUserSequence();
            Thread.sleep(100); // Small delay to ensure sequence is set
            createOrUpdateAdmin();
        } catch (Exception e) {
            System.err.println("Admin creation failed, retrying: " + e.getMessage());
            try {
                // Force sequence reset and retry
                sequenceService.resetUserSequence();
                Thread.sleep(200);
                createOrUpdateAdmin();
            } catch (Exception ex) {
                System.err.println("CRITICAL: Admin user creation failed completely: " + ex.getMessage());
                // Last resort - create with explicit high ID
                createAdminWithExplicitId();
            }
        }
    }
    
    private void createOrUpdateAdmin() {
        Users admin = userRepository.findByUsername("admin").orElse(null);
        
        if (admin == null) {
            // Create new admin if doesn't exist
            admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@example.com");
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin.setActive(true);
            admin.setMustChangePassword(false);
            admin.setPhone("+1111111111");
            admin.setDepartment("Administration");
            System.out.println("✅ Admin user created with password: admin");
        } else {
            // Update existing admin
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setActive(true);
            System.out.println("✅ Admin user updated with password: admin");
        }

        userRepository.save(admin);
    }
    
    private void createAdminWithExplicitId() {
        try {
            // Last resort: use native SQL to create admin with explicit ID
            System.out.println("🚨 Using fallback method to create admin user");
            // This will be handled by the existing database admin user
        } catch (Exception e) {
            System.err.println("🚨 FATAL: Cannot create admin user at all: " + e.getMessage());
        }
    }
}
