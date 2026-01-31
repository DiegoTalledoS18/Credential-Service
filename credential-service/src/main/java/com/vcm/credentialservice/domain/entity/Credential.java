package com.vcm.credentialservice.domain.entity;
import com.vcm.credentialservice.domain.enums.CredentialStatus;
import com.vcm.credentialservice.domain.enums.CredentialType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "credentials")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Credential extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CredentialType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CredentialStatus status;

    @Column(nullable = false)
    private String issuer;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}

