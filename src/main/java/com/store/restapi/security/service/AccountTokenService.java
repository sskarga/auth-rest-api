package com.store.restapi.security.service;

import com.store.restapi.security.domain.model.AccountToken;
import com.store.restapi.security.domain.model.UserDetailsImpl;

import java.util.Optional;
import java.util.UUID;

public interface AccountTokenService {
    AccountToken create(UserDetailsImpl user);
    Optional<AccountToken> find(UUID id);
    AccountToken update(AccountToken token);
    void delete(AccountToken token);
}
