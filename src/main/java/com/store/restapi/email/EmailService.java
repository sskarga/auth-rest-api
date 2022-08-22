package com.store.restapi.email;

import javax.mail.MessagingException;

public interface EmailService {
    public void sendMail(EmailContext email) throws MessagingException;
}
