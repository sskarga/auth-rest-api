package com.store.restapi.security.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountTokenRepository extends JpaRepository<AccountToken, UUID> {
    void deleteByExpiresAtLessThan(LocalDateTime dateTime);
    void deleteByAccountId(Long id);
}