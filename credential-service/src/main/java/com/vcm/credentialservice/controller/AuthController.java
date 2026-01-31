package com.vcm.credentialservice.controller;

import com.vcm.credentialservice.dto.AuthRequest;
import com.vcm.credentialservice.dto.AuthResponse;
import com.vcm.credentialservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid AuthRequest request) {
        userService.register(request.email(), request.password());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid AuthRequest request) {
        return new AuthResponse("fake-jwt-token-for-challenge");
    }
}
