package com.store.restapi.security.auth;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record RequestSignInDto(@NotBlank @Email @Length(min = 5, max = 60) String username,
                               @NotBlank @Length(min = 5, max = 60) String password) {
}
