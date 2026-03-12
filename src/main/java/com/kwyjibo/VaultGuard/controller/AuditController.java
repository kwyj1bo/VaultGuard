package com.kwyjibo.VaultGuard.controller;

import org.springframework.web.bind.annotation.PostMapping;
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
    public String logAction(@RequestParam Long requestId, @RequestParam String action){
        auditService.logAction(requestId, action);
        return "Action logged and analyzed.";
    }
}
