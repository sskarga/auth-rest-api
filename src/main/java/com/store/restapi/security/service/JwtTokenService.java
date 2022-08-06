package com.store.restapi.security.service;

import com.store.restapi.security.dto.ResponseTokenDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface JwtTokenService {
    Optional<UserDetails> validateTokenAndGetUser(String token);
    ResponseTokenDto singUp(String username, String password);
}
