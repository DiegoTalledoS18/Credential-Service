package com.vcm.credentialservice.service;

import com.vcm.credentialservice.domain.entity.Credential;
import com.vcm.credentialservice.domain.enums.CredentialStatus;
import com.vcm.credentialservice.domain.enums.CredentialType;
import com.vcm.credentialservice.dto.CredentialRequest;
import com.vcm.credentialservice.dto.CredentialResponse;
import com.vcm.credentialservice.dto.CursorResponse;
import com.vcm.credentialservice.repository.CredentialRepository;
import com.vcm.credentialservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final UserRepository userRepository;
    private final CredentialRepository repository;

    @Transactional
    public CredentialResponse create(CredentialRequest dto) {
        // Al no implementar JWT estamos dandole un valor provicional
        Long userId = userRepository.findRandomUserId()
                .orElseThrow(() -> new EntityNotFoundException("There is no users in the database"));

        Credential credential = Credential.builder()
                .userId(userId)
                .type(dto.type())
                .issuer(dto.issuer())
                .licenseNumber(dto.licenseNumber())
                .expiryDate(dto.expiryDate())
                .status(CredentialStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .createdBy("system")
                .build();

        Credential saved = repository.save(credential);

        return mapToResponse(saved);
    }

    public List<Credential> list(Long userId) {
        return repository.findByUserIdAndDeletedAtIsNull(userId);
    }

    public Credential get(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow();
    }

    public CredentialResponse getById(Long id) {
        return repository.findByIdAndDeletedAtIsNull(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Credential not found or is deleted"));
    }

    @Transactional(readOnly = true)
    public CursorResponse<CredentialResponse> listWithCursor(Long userId, CredentialType type, Long lastId, int size) {
        Pageable limit = PageRequest.of(0, size);

        List<CredentialResponse> data = repository.findWithCursor(userId, type, lastId, limit)
                .stream()
                .map(this::mapToResponse)
                .toList();

        Long nextCursor = data.isEmpty() ? null : data.get(data.size() - 1).id();

        return new CursorResponse<>(data, nextCursor, data.size());
    }

    @Transactional
    public void updateStatus(Long id, CredentialStatus newStatus) {
        Credential credential = repository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Credential not found or deleted"));

        credential.setStatus(newStatus);
        credential.setUpdatedAt(Instant.now());

        repository.save(credential);
    }

    public void softDelete(Long id) {
        Credential c = get(id);
        if (c.getStatus() == CredentialStatus.APPROVED) {
            throw new IllegalStateException("Cannot delete an APPROVED credential. Only PENDING or REJECTED are allowed.");
        }

        c.setDeletedAt(Instant.now());
        repository.save(c);
    }

    private CredentialResponse mapToResponse(Credential c) {
        return new CredentialResponse(
                c.getId(),
                c.getType(),
                c.getIssuer(),
                c.getLicenseNumber(),
                c.getExpiryDate(),
                c.getStatus(),
                c.getCreatedAt()
        );
    }
}

