package com.vcm.credentialservice.controller;

import com.vcm.credentialservice.dto.StatusUpdateRequest;
import com.vcm.credentialservice.service.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/credentials")
@RequiredArgsConstructor
public class AdminController {

    private final CredentialService service;

    @PutMapping("/{id}/status")
    public void updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        service.updateStatus(id, request.status());
    }
}