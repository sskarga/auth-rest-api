package com.store.restapi.security.service.impl;

import com.store.restapi.security.config.jwt.JwtConfig;
import com.store.restapi.security.domain.model.AccountToken;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.domain.repository.AccountTokenRepository;
import com.store.restapi.security.service.AccountTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountTokenServiceImpl implements AccountTokenService {

    private final AccountTokenRepository accountTokenRepository;
    private final JwtConfig jwtConfig;

    @Override
    public AccountToken create(UserDetailsImpl user) {
        AccountToken token = new AccountToken(user.getUserId(), jwtConfig.refreshExpiresAtLocalDateTime());
        return accountTokenRepository.save(token);
    }

    @Override
    public Optional<AccountToken> find(UUID id) {
        return accountTokenRepository.findById(id);
    }

    @Override
    public AccountToken update(AccountToken token) {
        return accountTokenRepository.save(token);
    }

    @Override
    public void delete(AccountToken token) {
        accountTokenRepository.delete(token);
    }
}
