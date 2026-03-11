package com.kwyjibo.VaultGuard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kwyjibo.VaultGuard.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
    
}
