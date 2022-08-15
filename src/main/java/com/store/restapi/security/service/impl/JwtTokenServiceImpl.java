package com.store.restapi.security.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.security.config.jwt.JwtProvider;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;


    @Override
    public Optional<UserDetails> validateTokenAndGetUser(String token) {
        DecodedJWT jwt = jwtProvider.verifyAccessToken(token);
        if (jwt == null) return Optional.empty();
        UserDetails user = UserDetailsImpl.builder()
                .userId(jwt.getClaim("id").asLong())
//                .username(jwt.getSubject())
                .fullUserName(jwt.getSubject())
                .enabled(true)
                .locked(false)
                .authorities(
                        Collections.singletonList(
                                new SimpleGrantedAuthority(jwt.getClaim("role").asString())
                        )
                )
                .build();
        return Optional.of(user);
//        return Optional.of(userDetailsService.loadUserByUsername(jwt.getSubject()));
    }


}
