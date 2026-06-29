package com.machy.repository;

import com.machy.entity.ScanSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ScanSessionRepository extends JpaRepository<ScanSession, UUID> {
    List<ScanSession> findBySessionIdOrderByCreatedAtDesc(String sessionId);
}
