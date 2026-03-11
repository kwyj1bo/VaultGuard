package com.kwyjibo.VaultGuard.service;

import org.springframework.stereotype.Service;

import com.kwyjibo.VaultGuard.model.User;
import com.kwyjibo.VaultGuard.repository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public User CreateUser(User user) {
        return userRepo.save(user);
    }
}
