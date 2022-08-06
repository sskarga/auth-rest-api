package com.store.restapi.security.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.security.config.jwt.JwtProvider;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.dto.ResponseTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public ResponseTokenDto singUp(String username, String password) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        UserDetailsImpl user = (UserDetailsImpl) authenticate.getPrincipal();

        return new ResponseTokenDto(
                jwtProvider.getAccessToken(user),
                jwtProvider.getRefreshToken(user)
        );
    }

    public ResponseTokenDto refresh(String refreshToken) {
        DecodedJWT jwt = jwtProvider.verifyRefreshToken(refreshToken);
        // TDOD: Exception
        if (jwt==null) throw new RuntimeException("Token invalid");
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(jwt.getSubject());
        if (!user.isEnabled()) throw new RuntimeException("User disable.");

        return new ResponseTokenDto(
                jwtProvider.getAccessToken(user),
                refreshToken
        );
    }
}
