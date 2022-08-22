package com.store.restapi.registration;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record RequestSignUpDto(
        @NotBlank @Length(min = 5, max = 60) String username,
        @NotBlank @Email @Length(min = 5, max = 60) String email,
        @NotBlank @Length(min = 5, max = 60) String password) {
}
