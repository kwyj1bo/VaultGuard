package com.kwyjibo.VaultGuard.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kwyjibo.VaultGuard.model.User;
import com.kwyjibo.VaultGuard.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.CreateUser(user);
    }
}
