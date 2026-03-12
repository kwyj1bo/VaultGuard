package com.kwyjibo.VaultGuard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kwyjibo.VaultGuard.model.AuditLog;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLog, Long> {

}
