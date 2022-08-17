package com.store.restapi.pincode;

import com.store.restapi.account.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinCodeService {

    private final PinCodeRepository pinCodeRepository;

    public PinCode createPinCode(Account account, CodeType codeType) {
        return pinCodeRepository.save(
                new PinCode(
                        account.getId(),
                        PinCodeGenerator.getPinCode(),
                        codeType,
                        LocalDateTime.now().plusHours(2) // Todo: To config file
                )
        );
    }

    public Optional<PinCode> findByAccount(Account account) {
        Optional<PinCode> pinCode = pinCodeRepository.findById(account.getId());
        if (pinCode.isPresent()) {
            if (pinCode.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                this.deleteByAccount(account);
                return Optional.empty();
            }
        }
        return pinCode;
    }

    public void deleteByAccount(Account account) {
        pinCodeRepository.deleteById(account.getId());
    }

    public void deleteById(Long id) {
        pinCodeRepository.deleteById(id);
    }

    @Scheduled(cron = "@daily")
    public void deleteAllExpired() {
        log.info("Run delete all expired pin code");
        pinCodeRepository.deleteByExpiresAtLessThan(LocalDateTime.now().plusMinutes(1));
    }

}
