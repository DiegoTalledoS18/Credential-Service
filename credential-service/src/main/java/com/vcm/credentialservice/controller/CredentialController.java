package com.vcm.credentialservice.controller;

import com.vcm.credentialservice.domain.enums.CredentialType;
import com.vcm.credentialservice.dto.CredentialRequest;
import com.vcm.credentialservice.dto.CredentialResponse;
import com.vcm.credentialservice.dto.CursorResponse;
import com.vcm.credentialservice.service.CredentialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CredentialResponse create(@RequestBody @Valid CredentialRequest request) {
        return service.create(request);
    }

    @GetMapping
    public CursorResponse<CredentialResponse> list(
            @RequestParam Long userId,
            @RequestParam(required = false) CredentialType type,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") int size) {
        return service.listWithCursor(userId, type, lastId, size);
    }

    @GetMapping("/{id}")
    public CredentialResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.softDelete(id);
    }

}