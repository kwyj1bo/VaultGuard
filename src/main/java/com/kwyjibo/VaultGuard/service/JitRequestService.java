package com.kwyjibo.VaultGuard.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;

import com.kwyjibo.VaultGuard.model.JitRequest;
import com.kwyjibo.VaultGuard.model.Server;
import com.kwyjibo.VaultGuard.model.User;
import com.kwyjibo.VaultGuard.repository.JitRequestRepo;
import com.kwyjibo.VaultGuard.repository.ServerRepo;
import com.kwyjibo.VaultGuard.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JitRequestService {
    private final JitRequestRepo jitRequestRepo;
    private final UserRepo userRepo;
    private final ServerRepo serverRepo;
    private final VaultTemplate vaultTemplate;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    public JitRequest createRequest(Long userId, Long serverId, int duration){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Server server = serverRepo.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found with ID: " + serverId));
        
        JitRequest request = new JitRequest();
        request.setUser(user);
        request.setServer(server);
        request.setDurationMinutes(duration);
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus("PENDING");

        log.info("📝 New JIT Request created for user: {} on server: {}", user.getUsername(), server.getHostName());
        return jitRequestRepo.save(request);
    }

    public JitRequest approveRequest(Long requestId){
        JitRequest request = jitRequestRepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found with ID: " + requestId));

        log.info("🔑 Attempting to fetch JIT credentials from Vault for Request ID: {}", requestId);
        var response = vaultTemplate.read("database/creds/readonly-role");

        if (response != null && response.getData() != null) {
            Map<String, Object> data = response.getData();
            String genUser = data.get("username").toString();
            String genPass = data.get("password").toString();

            request.setGeneratedUsername(genUser);
            request.setGeneratedPassword(genPass);
            request.setVaultLeaseId(response.getLeaseId());

            try {
                String sessionKey = "session:" + request.getId();
                redisTemplate.opsForValue().set(
                    sessionKey, 
                    response.getLeaseId(), 
                    request.getDurationMinutes(), 
                    TimeUnit.MINUTES
                );
                log.info("🚀 Redis session initialized for Request ID: {} with {} min TTL", requestId, request.getDurationMinutes());
            } catch (Exception redisEx) {
                log.error("❌ Redis Error: {}. Is Memurai running?", redisEx.getMessage());
            }

            request.setStatus("APPROVED");
            request.setApprovedAt(LocalDateTime.now());
            request.setExpiresAt(LocalDateTime.now().plusMinutes(request.getDurationMinutes()));

            emailService.sendCredentialsEmail(request.getUser().getEmail(), genUser, genPass);
            
            log.info("✅ Request #{} marked as APPROVED", requestId);
            
            return jitRequestRepo.save(request);
        } 
        else {
            log.error("❌ Vault returned NULL for path: database/creds/readonly-role");
            throw new RuntimeException("Vault failed to generate credentials.");
        }
    }
}