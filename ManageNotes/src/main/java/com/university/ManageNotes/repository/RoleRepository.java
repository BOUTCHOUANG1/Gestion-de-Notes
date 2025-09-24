package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.AppRole;
import com.university.ManageNotes.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(AppRole appRole);
}
