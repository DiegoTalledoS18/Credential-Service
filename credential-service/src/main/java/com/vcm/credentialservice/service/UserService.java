package com.vcm.credentialservice.service;

import com.vcm.credentialservice.domain.entity.User;
import com.vcm.credentialservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String email, String rawPassword) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .createdAt(Instant.now())
                .createdBy("system")
                .build();

        return userRepository.save(user);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

