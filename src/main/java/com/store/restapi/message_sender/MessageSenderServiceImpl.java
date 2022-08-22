package com.store.restapi.message_sender;

import com.store.restapi.account.Account;
import com.store.restapi.email.EmailConfig;
import com.store.restapi.email.EmailContext;
import com.store.restapi.email.EmailService;
import com.store.restapi.pincode.PinCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageSenderServiceImpl implements MessageSenderService {

    private final EmailService emailService;
    private final EmailConfig emailConfig;

    private EmailContext getEmailContext(Account account) {
        EmailContext context = new EmailContext();
        context.setFrom(emailConfig.getFrom());
        context.setTo(account.getEmail());

        Map<String, Object> templateVar = new HashMap<>();
        templateVar.put("username", account.getUsername());
        templateVar.put("resourceHost", emailConfig.getResourceHost());
        context.setTemplateContext(templateVar);
        return context;
    }

    @Override
    public void sendWelcomeMessage(Account account) throws MessagingException {
        EmailContext context = getEmailContext(account);
        context.setSubject("Welcome");
        context.setTemplateLocation("mail/registration/welcome");

        emailService.sendMail(context);
    }

    @Override
    public void sendActivateMessage(Account account, PinCode code) throws MessagingException {
        EmailContext context = getEmailContext(account);
        context.getTemplateContext().put("pincode", code.getCode());

        context.setSubject("Activate account - code " + code.getCode());
        context.setTemplateLocation("mail/registration/activate");

        emailService.sendMail(context);
    }

    @Override
    public void sendResetMessage(Account account, PinCode code) throws MessagingException {
        EmailContext context = getEmailContext(account);
        context.getTemplateContext().put("pincode", code.getCode());

        context.setSubject("Password reset - code " + code.getCode());
        context.setTemplateLocation("mail/password/forgot");

        emailService.sendMail(context);
    }

    @Override
    public void sendPasswordChanges(Account account) throws MessagingException {
        EmailContext context = getEmailContext(account);
        context.setSubject("Notification - password changes");
        context.setTemplateLocation("mail/password/notification");

        emailService.sendMail(context);
    }
}
