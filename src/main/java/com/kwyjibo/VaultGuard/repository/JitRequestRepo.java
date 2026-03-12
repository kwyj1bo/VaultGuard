package com.kwyjibo.VaultGuard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kwyjibo.VaultGuard.model.JitRequest;

@Repository
public interface JitRequestRepo extends JpaRepository<JitRequest, Long> {
    
}
