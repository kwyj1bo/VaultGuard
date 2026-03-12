package com.kwyjibo.VaultGuard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kwyjibo.VaultGuard.model.Server;

@Repository
public interface ServerRepo extends JpaRepository<Server, Long> {
    
}
