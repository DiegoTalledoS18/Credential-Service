package com.vcm.credentialservice.scheduler;

import com.vcm.credentialservice.repository.CredentialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class CredentialExpirationJob {

    private final CredentialRepository credentialRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void expireCredentials() {
        Instant now = Instant.now();
        Instant graceThreshold = now.minus(Duration.ofDays(14));

        int updatedCount = credentialRepository.expireEligibleCredentials(now, graceThreshold);
        log.info("Expired credentials: {}", updatedCount);
    }

}
