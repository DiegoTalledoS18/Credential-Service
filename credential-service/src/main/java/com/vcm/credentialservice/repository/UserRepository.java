package com.vcm.credentialservice.repository;

import com.vcm.credentialservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT id FROM users ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Long> findRandomUserId();
}

