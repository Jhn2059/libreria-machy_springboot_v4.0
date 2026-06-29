package com.machy.repository;

import com.machy.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface LogRepository extends JpaRepository<LogEntry, UUID> {
}
