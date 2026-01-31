package com.vcm.credentialservice.repository;
import com.vcm.credentialservice.domain.entity.Credential;
import com.vcm.credentialservice.domain.enums.CredentialType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential> findByUserIdAndDeletedAtIsNull(Long userId);
    Optional<Credential> findByIdAndDeletedAtIsNull(Long id);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE credentials c
    SET status = 'EXPIRED', updated_at = NOW()
    WHERE status = 'APPROVED'
      AND deleted_at IS NULL
      AND (
          (expiry_date < :now AND NOT EXISTS (
              SELECT 1 FROM credentials r 
              WHERE r.user_id = c.user_id 
                AND r.type = c.type 
                AND r.status = 'PENDING' 
                AND r.deleted_at IS NULL
          ))
          OR 
          (expiry_date <= :graceThreshold)
      )
    """, nativeQuery = true)
    int expireEligibleCredentials(Instant now, Instant graceThreshold);

    // for pagination
    @Query("""
        SELECT c FROM Credential c 
        WHERE c.userId = :userId 
          AND c.deletedAt IS NULL 
          AND (:type IS NULL OR c.type = :type)
          AND (:lastId IS NULL OR c.id > :lastId)
        ORDER BY c.id ASC
    """)
    List<Credential> findWithCursor(Long userId, CredentialType type, Long lastId, Pageable pageable);
}
