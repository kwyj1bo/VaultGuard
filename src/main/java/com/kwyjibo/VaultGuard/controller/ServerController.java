package com.kwyjibo.VaultGuard.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kwyjibo.VaultGuard.model.Server;
import com.kwyjibo.VaultGuard.service.ServerService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
public class ServerController {

    private final ServerService serverService;

    @PostMapping("/add")
    public Server addServer(@RequestBody Server server) {
        return serverService.addServer(server);
    }

    @GetMapping
    public List<Server> getAllServers() {
        return serverService.getAllServers();
    }   
}
