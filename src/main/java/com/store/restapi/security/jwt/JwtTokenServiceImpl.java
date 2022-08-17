package com.store.restapi.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.store.restapi.security.user_details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtProvider jwtProvider;

    @Override
    public Optional<UserDetails> validateTokenAndGetUser(String token) {
        DecodedJWT jwt = jwtProvider.verifyAccessToken(token);
        if (jwt == null) return Optional.empty();
        UserDetails user = UserDetailsImpl.builder()
                .userId(jwt.getClaim("id").asLong())
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
    }


}
