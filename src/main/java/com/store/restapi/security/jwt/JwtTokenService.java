package com.store.restapi.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtTokenService {
    Optional<UserDetails> validateTokenAndGetUser(String token);
}
