package com.store.restapi.security.refresh_token.dto;

import javax.validation.constraints.NotBlank;

public record RequestRefreshTokenDto(@NotBlank String refreshToken) { }
