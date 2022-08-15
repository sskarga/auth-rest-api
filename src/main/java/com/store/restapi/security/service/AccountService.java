package com.store.restapi.security.service;

import com.store.restapi.security.domain.model.Role;
import com.store.restapi.security.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getUsers();

    Optional<Account> loadUserById(Long id);
    Optional<Account> loadUserByEmail(String email);

    Account createUser(Account user);

    Account updateUser(Account user);

    Account deleteUser(Long id);

    Role saveRole(Role role);
    Account addRoleToUser(String email, String roleName);
}
