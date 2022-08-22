package com.store.restapi.registration;

import com.store.restapi.account.Account;
import com.store.restapi.account.AccountService;
import com.store.restapi.advice.exception.CustomApiException;
import com.store.restapi.message_sender.MessageSenderService;
import com.store.restapi.pincode.CodeType;
import com.store.restapi.pincode.PinCode;
import com.store.restapi.pincode.PinCodeService;
import com.store.restapi.security.refresh_token.AccountTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final AccountService accountService;
    private final PinCodeService pinCodeService;
    private final MessageSenderService messageSenderService;
    private final AccountTokenService accountTokenService;

    private Account getAccount(String email) {
        Account account = accountService.findAccountByEmail(email).orElseThrow(EntityNotFoundException::new);
        if (account.getLocked()) throw new CustomApiException("Аккаунт заблокирован.");
        return account;
    }

    private PinCode getPinCode(Integer checkPinCode, Account account) {
        PinCode pinCode = pinCodeService.findByAccount(account).orElseThrow(EntityNotFoundException::new);

        if (pinCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            pinCodeService.deleteByAccount(account);
            throw new CustomApiException("Срок действия пинкода истек.");
        }

        if(!pinCode.getCode().equals(checkPinCode)) {
            throw new CustomApiException("Неверный пинкод.");
        }
        return pinCode;
    }
    
    @Override
    public void createAccount(Account newAccount) {
        Account account = accountService.createAccount(newAccount);
        PinCode pinCode = pinCodeService.createPinCode(account, CodeType.CODE_ACTIVATE);
        log.info("Activate pin code: {}",pinCode.getCode());

        try {
            messageSenderService.sendActivateMessage(account,pinCode);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ReActivateAccount(String email) {
        Account account = getAccount(email);
        if (account.getEnabled()) throw new CustomApiException("Аккаунт уже активирован.");

        PinCode pinCode = pinCodeService.createPinCode(account, CodeType.CODE_ACTIVATE);
        log.info("Re activate pin code: {}",pinCode.getCode());

        try {
            messageSenderService.sendActivateMessage(account,pinCode);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void activateAccount(String email, Integer checkPinCode) {
        Account account = getAccount(email);
        if (account.getEnabled()) throw new CustomApiException("Аккаунт уже активирован.");

        PinCode pinCode = getPinCode(checkPinCode, account);
        if (!pinCode.getCodeType().equals(CodeType.CODE_ACTIVATE)) throw new EntityNotFoundException();

        account.setEnabled(true);
        accountService.updateAccount(account);
        pinCodeService.deleteByAccount(account);

        try {
            messageSenderService.sendWelcomeMessage(account);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetPassword(String email) {
        Account account = getAccount(email);
        if (!account.getEnabled()) throw new CustomApiException("Аккаунт не активирован.");

        PinCode pinCode = pinCodeService.createPinCode(account, CodeType.CODE_PASSWORD_RESET);
        log.info("Reset pin code: {}",pinCode.getCode());

        try {
            messageSenderService.sendResetMessage(account,pinCode);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void changePassword(String email, Integer checkPinCode, String newPassword) {
        Account account = getAccount(email);
        PinCode pinCode = getPinCode(checkPinCode, account);
        if (!pinCode.getCodeType().equals(CodeType.CODE_PASSWORD_RESET)) throw new EntityNotFoundException();

        account.setPassword(newPassword);
        accountService.updateAccountAndPassword(account);
        pinCodeService.deleteByAccount(account);
        accountTokenService.deleteByAccountId(account.getId());

        try {
            messageSenderService.sendPasswordChanges(account);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }



}
