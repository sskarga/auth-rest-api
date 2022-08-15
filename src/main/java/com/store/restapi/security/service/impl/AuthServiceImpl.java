package com.store.restapi.security.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.security.config.jwt.JwtProvider;
import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.domain.model.AccountToken;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.dto.ResponseTokenDto;
import com.store.restapi.security.service.AccountService;
import com.store.restapi.security.service.AccountTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final AccountTokenService accountTokenService;
    private final AccountService accountService;


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

    public ResponseTokenDto refresh(String refreshToken) {
        DecodedJWT jwt = jwtProvider.verifyRefreshToken(refreshToken);

        // TODO: Exception
        if (jwt==null) throw new RuntimeException("Token invalid");

        AccountToken accountToken = accountTokenService.find(UUID.fromString(jwt.getId()))
                .orElseThrow(() -> new RuntimeException("Token not find"));
        // TODO: Delete all ExpiresAtDateTime

        Account account = accountService.loadUserById(accountToken.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not find"));
        // TODO: delete all token by accountId

        UserDetailsImpl user = new UserDetailsImpl(account);
        if (!user.isEnabled() || !user.isAccountNonLocked()) throw new RuntimeException("User disable.");

        // TODO: if need update accountToken
        return new ResponseTokenDto(
                jwtProvider.getAccessToken(user),
                refreshToken
        );
    }

    public void logout(String refreshToken) {
        DecodedJWT jwt = jwtProvider.verifyRefreshToken(refreshToken);

        if (jwt==null) return;
        accountTokenService.find(UUID.fromString(jwt.getId())).ifPresent(accountTokenService::delete);
    }
}
