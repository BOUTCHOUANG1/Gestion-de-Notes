package com.university.ManageNotes.repository;

import com.university.ManageNotes.model.InviteToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteTokenRepository extends JpaRepository<InviteToken, Long> {
    Optional<InviteToken> findByTokenAndClaimedFalse(String token);
}
