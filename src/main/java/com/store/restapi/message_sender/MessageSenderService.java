package com.store.restapi.message_sender;

import com.store.restapi.account.Account;
import com.store.restapi.pincode.PinCode;

import javax.mail.MessagingException;

public interface MessageSenderService {
    void sendWelcomeMessage(Account account) throws MessagingException;
    void sendActivateMessage(Account account, PinCode code) throws MessagingException;

    void sendResetMessage(Account account, PinCode code) throws MessagingException;
    void sendPasswordChanges(Account account) throws MessagingException;
}
