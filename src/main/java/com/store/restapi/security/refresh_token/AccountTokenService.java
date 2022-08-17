package com.store.restapi.security.refresh_token;

import com.store.restapi.security.refresh_token.AccountToken;
import com.store.restapi.security.user_details.UserDetailsImpl;

import java.util.Optional;
import java.util.UUID;

public interface AccountTokenService {
    AccountToken create(UserDetailsImpl user);
    Optional<AccountToken> find(UUID id);

    void delete(AccountToken token);

    void deleteById(UUID id);

    void deleteByAccountId(Long id);

    void deleteExpiresToken();
}
