package com.kwyjibo.VaultGuard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kwyjibo.VaultGuard.model.Server;
import com.kwyjibo.VaultGuard.repository.ServerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServerService {
    private final ServerRepo serverRepo;
    public Server addServer(Server server) {
        return serverRepo.save(server);
    }
    public List<Server> getAllServers() {
        return serverRepo.findAll();
    }
}
