package com.vcm.credentialservice.dto;

import com.vcm.credentialservice.domain.enums.CredentialStatus;
import com.vcm.credentialservice.domain.enums.CredentialType;

import java.time.Instant;

public record CredentialResponse(
        Long id,
        CredentialType type,
        String issuer,
        String licenseNumber,
        Instant expiryDate,
        CredentialStatus status,
        Instant createdAt
) {}
