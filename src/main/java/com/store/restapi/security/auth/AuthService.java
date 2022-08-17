package com.store.restapi.security.auth;

import com.store.restapi.security.refresh_token.dto.ResponseTokenDto;

public interface AuthService {
    ResponseTokenDto signIn(String username, String password);

    ResponseTokenDto refresh(String refreshToken);

    void logout(String refreshToken);
}
