package com.store.restapi.pincode;

import com.store.restapi.account.Account;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Optional;

public interface PinCodeService {
    PinCode createPinCode(Account account, CodeType codeType);

    Integer updateResidueAttempts(Account account);

    Optional<PinCode> findByAccount(Account account);

    void deleteByAccount(Account account);

    void deleteById(Long id);

    void deleteAllExpired();
}
