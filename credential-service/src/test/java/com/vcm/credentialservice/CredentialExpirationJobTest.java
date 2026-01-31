package com.vcm.credentialservice;

import com.vcm.credentialservice.domain.entity.Credential;
import com.vcm.credentialservice.domain.enums.CredentialStatus;
import com.vcm.credentialservice.domain.enums.CredentialType;
import com.vcm.credentialservice.repository.CredentialRepository;
import com.vcm.credentialservice.scheduler.CredentialExpirationJob;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class CredentialExpirationJobTest {

    @Autowired private CredentialRepository repository;
    @Autowired private CredentialExpirationJob job;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("Caso A: Expiración inmediata si no hay renovación")
    void shouldExpireImmediately() {
        Credential c = create(CredentialStatus.APPROVED, Instant.now().minus(Duration.ofDays(365 * 10)));
        repository.save(c);

        entityManager.flush();
        entityManager.clear();

        job.expireCredentials();

        Credential updated = repository.findById(c.getId()).orElseThrow();
        assertEquals(CredentialStatus.EXPIRED, updated.getStatus());
    }

    @Test
    @DisplayName("Caso B: Aplicar periodo de gracia (14 días) si hay una renovación PENDING")
    void shouldDelayExpirationDuringGracePeriod() {
        // 1: Expiró hace 2 días (dentro del rango de 14)
        Credential old = create(CredentialStatus.APPROVED, Instant.now().minus(Duration.ofDays(2)));
        // 2: El usuario ya mandó una nueva que está PENDING
        Credential pending = create(CredentialStatus.PENDING, Instant.now().plus(Duration.ofDays(30)));

        repository.saveAll(List.of(old, pending));

        job.expireCredentials();

        // 3: La vieja NO debe expirar todavía
        assertEquals(CredentialStatus.APPROVED, repository.findById(old.getId()).get().getStatus());
    }

    private Credential create(CredentialStatus status, Instant expiry) {
        return Credential.builder()
                .userId(1L).type(CredentialType.HVAC_LICENSE).status(status)
                .expiryDate(expiry).issuer("Test").licenseNumber("123").createdBy("admin")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}