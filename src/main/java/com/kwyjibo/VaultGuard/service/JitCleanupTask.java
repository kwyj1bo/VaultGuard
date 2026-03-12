package com.kwyjibo.VaultGuard.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kwyjibo.VaultGuard.model.JitRequest;
import com.kwyjibo.VaultGuard.repository.JitRequestRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JitCleanupTask {

    private final JitRequestRepo jitRequestRepo;

    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredRequests(){
        log.info("Janitor Heartbeat - Current System Time: " + java.time.LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        List<JitRequest> expiredRequests = jitRequestRepo.findAll().stream()
                .filter(r -> "APPROVED".equals(r.getStatus()) && r.getExpiresAt().isBefore(now))
                .toList();
        
        if(!expiredRequests.isEmpty()){
            log.info("Found {} expired requests. Revoking access...", expiredRequests.size());
        }
        for (JitRequest request : expiredRequests) {
            request.setStatus("EXPIRED");
            jitRequestRepo.save(request);
        }
    }
}
