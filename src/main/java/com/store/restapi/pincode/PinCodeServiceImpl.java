package com.store.restapi.pincode;

import com.store.restapi.account.Account;
import com.store.restapi.advice.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinCodeServiceImpl implements PinCodeService {

    public static final String REFRESH_EXCEPTION_WAIT_MSG = "Код уже отправлен. Новый код можно получить через %s минут";
    public static final long PIN_CODE_REFRESH_MINUTES = 10;
    public static final long PIN_CODE_MAX_ATTEMPTS = 5;

    private final PinCodeRepository pinCodeRepository;

    @Override
    public PinCode createPinCode(Account account, CodeType codeType) {

        Optional<PinCode> findPinCode = this.findByAccount(account);
        if (findPinCode.isPresent()) {
            if (findPinCode.get().getCreatedOn().plusMinutes(PIN_CODE_REFRESH_MINUTES).isBefore(LocalDateTime.now())) {
                deleteByAccount(account);
            } else {
                Long refreshCodeMinutes = PIN_CODE_REFRESH_MINUTES - Duration.between(
                        findPinCode.get().getCreatedOn(),
                        LocalDateTime.now()
                ).toMinutes();
                throw new CustomApiException(String.format(REFRESH_EXCEPTION_WAIT_MSG, refreshCodeMinutes));
            }
        }

        return pinCodeRepository.save(
                new PinCode(
                        account.getId(),
                        PinCodeGenerator.getPinCode(),
                        codeType,
                        LocalDateTime.now().plusHours(2) // Todo: To config file
                )
        );
    }

    @Override
    @Transactional
    public Integer updateResidueAttempts(Account account) {
        PinCode pinCode = this.findByAccount(account).orElseThrow(EntityNotFoundException::new);
        if (pinCode.getAttempts() == null) {
            pinCode.setAttempts(1);
        } else {
            pinCode.setAttempts(pinCode.getAttempts() + 1);
        }

        pinCodeRepository.save(pinCode);

        return  (int) (PIN_CODE_MAX_ATTEMPTS - pinCode.getAttempts());
    }

    @Override
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

    @Override
    public void deleteByAccount(Account account) {
        pinCodeRepository.deleteById(account.getId());
    }

    @Override
    public void deleteById(Long id) {
        pinCodeRepository.deleteById(id);
    }

    @Override
    public void deleteAllExpired() {
        pinCodeRepository.deleteByExpiresAtLessThan(LocalDateTime.now().plusMinutes(1));
    }

}
