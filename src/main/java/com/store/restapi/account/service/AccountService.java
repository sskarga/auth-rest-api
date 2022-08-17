package com.store.restapi.account.service;

import com.store.restapi.account.domain.Role;
import com.store.restapi.account.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();

    Optional<Account> findAccountById(Long id);
    Optional<Account> findAccountByEmail(String email);

    Account createAccount(Account user);

    Account updateAccount(Account user);

    Account deleteAccount(Long id);

    List<Role> getAllRole();
    Role saveRole(Role role);
    Account addRoleToAccount(Account account, String roleName);
}
