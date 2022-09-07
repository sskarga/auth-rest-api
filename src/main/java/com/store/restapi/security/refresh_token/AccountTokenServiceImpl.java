package com.store.restapi.security.refresh_token;

import com.store.restapi.security.jwt.JwtConfig;
import com.store.restapi.security.user_details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
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
        Optional<AccountToken> token = accountTokenRepository.findById(id);
        if (token.isPresent()) {
            if (token.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                this.deleteById(id);
                return Optional.empty();
            }
        }
        return token;
    }

    @Override
    public void delete(AccountToken token) {
        accountTokenRepository.delete(token);
    }

    @Override
    public void deleteById(UUID id) {
        accountTokenRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByAccountId(Long id) {
        accountTokenRepository.deleteByAccountId(id);
    }

    @Override

    public void deleteExpiresToken() {
        LocalDateTime expires = LocalDateTime.now().plusMinutes(1);
        accountTokenRepository.deleteByExpiresAtLessThan(expires);
    }
}
