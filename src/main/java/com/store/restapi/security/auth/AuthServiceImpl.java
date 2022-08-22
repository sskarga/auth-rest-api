package com.store.restapi.security.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.advice.exception.CustomApiException;
import com.store.restapi.security.jwt.JwtProvider;
import com.store.restapi.account.Account;
import com.store.restapi.security.refresh_token.AccountToken;
import com.store.restapi.security.user_details.UserDetailsImpl;
import com.store.restapi.security.refresh_token.dto.ResponseTokenDto;
import com.store.restapi.account.AccountService;
import com.store.restapi.security.refresh_token.AccountTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    public static final String TOKEN_INVALID_MSG = "Token invalid.";
    public static final String USER_LOCKED_MSG = "Account locked.";
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final AccountTokenService accountTokenService;
    private final AccountService accountService;


    @Override
    public ResponseTokenDto signIn(String username, String password) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetailsImpl user = (UserDetailsImpl) authenticate.getPrincipal();
        AccountToken accountToken = accountTokenService.create(user);

        return new ResponseTokenDto(
                jwtProvider.getAccessToken(user),
                jwtProvider.getRefreshToken(accountToken.getId().toString())
        );
    }

    @Override
    public ResponseTokenDto refresh(String refreshToken) {
        log.info("Refresh token");
        DecodedJWT jwt = jwtProvider.verifyRefreshToken(refreshToken);

        if (jwt==null) {
            throw new CustomApiException(TOKEN_INVALID_MSG);
        }

        AccountToken accountToken = accountTokenService.find(UUID.fromString(jwt.getId()))
                .orElseThrow(() -> new EntityNotFoundException(TOKEN_INVALID_MSG));

        Account account = accountService.findAccountById(accountToken.getAccountId())
                .orElseThrow(
                        () -> {
                            log.error("Not find account by id {}.", accountToken.getAccountId());
                            accountTokenService.deleteByAccountId(accountToken.getAccountId());
                            throw new EntityNotFoundException(TOKEN_INVALID_MSG);
                        }
                );

        UserDetailsImpl user = new UserDetailsImpl(account);
        if (!user.isEnabled() || !user.isAccountNonLocked()) throw new CustomApiException(USER_LOCKED_MSG);


        return new ResponseTokenDto(
                jwtProvider.getAccessToken(user),
                refreshToken
        );
    }

    @Override
    public void logout(String refreshToken) {
        DecodedJWT jwt = jwtProvider.verifyRefreshToken(refreshToken);

        if (jwt==null) return;
        accountTokenService.deleteById(UUID.fromString(jwt.getId()));
    }
}
