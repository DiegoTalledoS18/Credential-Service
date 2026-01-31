package com.vcm.credentialservice.dto;

import com.vcm.credentialservice.domain.enums.CredentialStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Status is required")
        CredentialStatus status
) {}