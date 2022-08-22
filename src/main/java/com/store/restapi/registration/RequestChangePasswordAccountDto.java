package com.store.restapi.registration;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public record RequestChangePasswordAccountDto(@NotBlank @Email @Length(min = 5, max = 60) String email,
                                              @NotBlank @Length(min = 5, max = 60) String password,
                                              @Digits(message = "Invalid PINCode", integer = 6, fraction = 0) Integer pinCode) {
}
