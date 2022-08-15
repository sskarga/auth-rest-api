package com.store.restapi.security.domain.repository;

import com.store.restapi.security.domain.model.AccountToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountTokenRepository extends JpaRepository<AccountToken, UUID> {
    // equals
//    deleteByExpiresAtEquals(LocalDateTime dateTime);

    // lessthan
    void deleteByExpiresAtLessThan(LocalDateTime dateTime);

    // greaterthan
    void deleteByExpiresAtGreaterThan(LocalDateTime dateTime);
}