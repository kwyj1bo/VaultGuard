package com.kwyjibo.VaultGuard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kwyjibo.VaultGuard.service.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor

public class AuditController {
    private final AuditService auditService;

    @PostMapping("/log")
    public String logAction(@RequestBody java.util.Map<String, Object> payload){
        Long requestId = Long.valueOf(payload.get("requestId").toString());
        String action = (String) payload.get("action");
        auditService.logAction(requestId, action);
        return "Action logged and analyzed.";
    }
}
