package com.store.restapi.pincode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
    void deleteByExpiresAtLessThan(LocalDateTime dateTime);
}
