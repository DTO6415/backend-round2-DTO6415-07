package org.example.practice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Service

public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public PasswordService(
            @Value("${spring.application.algorithm}") String algorithm) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        PasswordEncoder selected = encoders.get(algorithm);

        if (selected == null) {
            throw new IllegalArgumentException(
                    "不支持的密码算法: " + algorithm
            );
        }

        this.passwordEncoder = selected;
    }
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }
    public boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
