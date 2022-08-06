package com.store.restapi.security.api.dto;

import javax.validation.constraints.NotBlank;

public record RequestRefreshToken (@NotBlank String refreshToken) { }
