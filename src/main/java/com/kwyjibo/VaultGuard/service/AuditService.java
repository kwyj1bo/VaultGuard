package com.kwyjibo.VaultGuard.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;

import com.kwyjibo.VaultGuard.model.AuditLog;
import com.kwyjibo.VaultGuard.model.JitRequest;
import com.kwyjibo.VaultGuard.repository.AuditLogRepo;
import com.kwyjibo.VaultGuard.repository.JitRequestRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepo auditLogRepo;
    private final JitRequestRepo jitRequestRepo;
    private final VaultTemplate vaultTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private static final List<String> BLACKLIST = Arrays.asList("DROP", "DELETE", "TRUNCATE", "GRANT");
    
    public void logAction(Long requestId, String action){
        JitRequest request = jitRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    
        AuditLog audit = new AuditLog();
        audit.setJitRequest(request);
        audit.setActionPerformed(action);
        audit.setTimeStamp(LocalDateTime.now());

        boolean sus = BLACKLIST.stream().anyMatch(word -> action.toUpperCase().contains(word));
        audit.setSuspicious(sus);
        auditLogRepo.save(audit);
        
        if (sus) {
            log.warn("🚨 SUSPICIOUS ACTIVITY: {}. Revoking via Vault...", action);
            revokeAccess(request, action);
        }
    }

    private void revokeAccess(JitRequest request, String action) {
        request.setStatus("REVOKED");
        jitRequestRepo.save(request);

        messagingTemplate.convertAndSend("/topic/alerts", 
            "REVOKED: Request ID " + request.getId() + " attempted " + action);
        if (request.getVaultLeaseId() != null) {
            try {
                vaultTemplate.write("sys/leases/revoke", Map.of("lease_id", request.getVaultLeaseId()));
                log.info("Vault lease {} revoked for Request ID {}", request.getVaultLeaseId(), request.getId());
            } catch (Exception e) {
                log.error("Failed to revoke Vault lease: " + e.getMessage());
            }
        }
    }
}