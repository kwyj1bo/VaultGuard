package com.kwyjibo.VaultGuard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   
@ComponentScan(basePackages = "com.kwyjibo.VaultGuard")
public class VaultGuardApplication {
    public static void main(String[] args) {
        SpringApplication.run(VaultGuardApplication.class, args);
    }
}
