package com.kwyjibo.VaultGuard.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kwyjibo.VaultGuard.model.JitRequest;
import com.kwyjibo.VaultGuard.model.Server;
import com.kwyjibo.VaultGuard.model.User;
import com.kwyjibo.VaultGuard.repository.JitRequestRepo;
import com.kwyjibo.VaultGuard.repository.ServerRepo;
import com.kwyjibo.VaultGuard.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JitRequestService {
    private final JitRequestRepo jitRequestRepo;
    private final UserRepo userRepo;
    private final ServerRepo serverRepo;

    public JitRequest createRequest(Long userId, Long serverId, int duration){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Server server = serverRepo.findById(serverId)
                .orElseThrow(() -> new RuntimeException("Server not found"));
        
        JitRequest request = new JitRequest();
        request.setUser(user);
        request.setServer(server);
        request.setDurationMinutes(duration);
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus("PENDING");

        return jitRequestRepo.save(request);

    }

    public JitRequest approveRequest(Long requestId){
        JitRequest request = jitRequestRepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));
    
        request.setStatus("APPROVED");
        request.setApprovedAt(LocalDateTime.now());
        request.setExpiresAt(LocalDateTime.now().plusMinutes(request.getDurationMinutes()));
        return jitRequestRepo.save(request);
    }
}
