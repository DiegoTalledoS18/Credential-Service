package com.vcm.credentialservice.dto;

import com.vcm.credentialservice.domain.enums.CredentialType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CredentialRequest(
        @NotNull CredentialType type,
        @NotBlank String issuer,
        @NotBlank String licenseNumber,
        @NotNull @Future Instant expiryDate
) {}
