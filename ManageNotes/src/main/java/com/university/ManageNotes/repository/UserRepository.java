package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.AppRole;
import com.university.ManageNotes.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    List<Users> findByRole(AppRole appRole);

    boolean existsByUserName(@NotBlank @Size(min =  3, max = 50) String username);

    boolean existsByEmail(@NotBlank @Email @Size(max = 50) String email);
}
