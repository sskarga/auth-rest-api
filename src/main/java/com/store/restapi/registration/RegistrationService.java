package com.store.restapi.registration;

import com.store.restapi.account.Account;

public interface RegistrationService {
    void createAccount(Account newAccount);

    void ReActivateAccount(String email);

    void activateAccount(String email, Integer checkPinCode);

    void resetPassword(String email);

    void changePassword(String email, Integer checkPinCode, String newPassword);
}
