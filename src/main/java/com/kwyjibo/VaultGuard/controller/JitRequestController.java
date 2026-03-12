package com.kwyjibo.VaultGuard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kwyjibo.VaultGuard.model.JitRequest;
import com.kwyjibo.VaultGuard.service.JitRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jit")
@RequiredArgsConstructor
public class JitRequestController {
    private final JitRequestService jitRequestService;

    @PostMapping("/request")
    public JitRequest requestAccess(
            @RequestParam Long userId, 
            @RequestParam Long serverId, 
            @RequestParam int duration) {
        return jitRequestService.createRequest(userId, serverId, duration);
    }
}
