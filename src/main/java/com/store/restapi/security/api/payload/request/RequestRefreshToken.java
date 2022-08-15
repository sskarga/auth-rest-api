package com.store.restapi.security.api.payload.request;

import javax.validation.constraints.NotBlank;

public record RequestRefreshToken (@NotBlank String refreshToken) { }
