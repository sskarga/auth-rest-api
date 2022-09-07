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

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    public static final String ACCOUNT_IS_LOCKED_MSG = "Аккаунт заблокирован.";
    public static final String CODE_INVALID_ACCOUNT_LOCKED_MSG = "Неверный код. Аккаунт заблокирован.";
    public static final String CODE_INVALID = "Неверный код. Осталось попыток %d до блокировки аккаунта.";
    public static final String ACCOUNT_IS_ACTIVATED_MSG = "Аккаунт уже активирован.";
    public static final String ACCOUNT_NOT_ACTIVATED_MSG = "Аккаунт не активирован.";
    public static final String ERROR_SEND_MAIL_MSG = "Ошибка отправки email";
    private final AccountService accountService;
    private final PinCodeService pinCodeService;
    private final MessageSenderService messageSenderService;
    private final AccountTokenService accountTokenService;

    private Account getAccount(String email) {
        Account account = accountService.findAccountByEmail(email).orElseThrow(EntityNotFoundException::new);
        if (account.getLocked()) throw new CustomApiException(ACCOUNT_IS_LOCKED_MSG);
        return account;
    }

    private PinCode getPinCode(Integer checkPinCode, Account account) {
        PinCode pinCode = pinCodeService.findByAccount(account).orElseThrow(EntityNotFoundException::new);

        if(!pinCode.getCode().equals(checkPinCode)) {
            Integer residueAttempts = pinCodeService.updateResidueAttempts(account);
            if (residueAttempts <= 0) {
                // Account block
                resetAccountTokens(account);
                account.setLocked(true);
                accountService.updateAccount(account);
                throw new CustomApiException(CODE_INVALID_ACCOUNT_LOCKED_MSG);
            }
            throw new CustomApiException(String.format(CODE_INVALID, residueAttempts));
        }
        return pinCode;
    }

    private void resetAccountTokens(Account account) {
        if (!Objects.isNull(account)) {
            pinCodeService.deleteByAccount(account);
            accountTokenService.deleteByAccountId(account.getId());
        }
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
            throw new CustomApiException(ERROR_SEND_MAIL_MSG, e);
        }
    }

    @Override
    public void ReActivateAccount(String email) {
        Account account = getAccount(email);
        if (account.getEnabled()) throw new CustomApiException(ACCOUNT_IS_ACTIVATED_MSG);

        PinCode pinCode = pinCodeService.createPinCode(account, CodeType.CODE_ACTIVATE);
        log.info("Re activate pin code: {}",pinCode.getCode());

        try {
            messageSenderService.sendActivateMessage(account,pinCode);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new CustomApiException(ERROR_SEND_MAIL_MSG, e);
        }
    }

    @Override
    public void activateAccount(String email, Integer checkPinCode) {
        Account account = getAccount(email);
        if (account.getEnabled()) throw new CustomApiException(ACCOUNT_IS_ACTIVATED_MSG);

        PinCode pinCode = getPinCode(checkPinCode, account);
        if (!pinCode.getCodeType().equals(CodeType.CODE_ACTIVATE)) throw new EntityNotFoundException();

        account.setEnabled(true);
        accountService.updateAccount(account);
        pinCodeService.deleteByAccount(account);

        try {
            messageSenderService.sendWelcomeMessage(account);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new CustomApiException(ERROR_SEND_MAIL_MSG, e);
        }
    }

    @Override
    public void resetPassword(String email) {
        Account account = getAccount(email);
        if (!account.getEnabled()) throw new CustomApiException(ACCOUNT_NOT_ACTIVATED_MSG);

        PinCode pinCode = pinCodeService.createPinCode(account, CodeType.CODE_PASSWORD_RESET);
        log.info("Reset pin code: {}",pinCode.getCode());

        try {
            messageSenderService.sendResetMessage(account,pinCode);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new CustomApiException(ERROR_SEND_MAIL_MSG, e);
        }
    }


    @Override
    public void changePassword(String email, Integer checkPinCode, String newPassword) {
        Account account = getAccount(email);
        PinCode pinCode = getPinCode(checkPinCode, account);
        if (!pinCode.getCodeType().equals(CodeType.CODE_PASSWORD_RESET)) throw new EntityNotFoundException();

        account.setPassword(newPassword);
        accountService.updateAccountAndPassword(account);
        resetAccountTokens(account);

        try {
            messageSenderService.sendPasswordChanges(account);
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new CustomApiException(ERROR_SEND_MAIL_MSG, e);
        }
    }



}
