package com.store.restapi.registration;

import com.store.restapi.account.Account;
import com.store.restapi.advice.api_error.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@Slf4j
public class RegistrationController {

    public static final String ACCOUNT_CREATE_MSG = "Аккаунт создан. На вашу почту выслан код активации.";
    private static final String ACCOUNT_ACTIVATE_MSG = "Аккаунт активирован.";
    private static final String ACCOUNT_RESET_PASSWORD_MSG = "На вашу почту выслан код для смены пароля";
    private static final String ACCOUNT_CHANGE_PASSWORD_MSG = "Пароль изменен";
    private final RegistrationService registrationService;

    @PostMapping("/singup")
    public ResponseEntity<ResponseMessageDto> singUp(@RequestBody @Valid RequestSignUpDto request) {
        registrationService.createAccount(new Account(request.email(), request.password(), request.username()));
        return new ResponseEntity<>(new ResponseMessageDto(HttpStatus.OK, ACCOUNT_CREATE_MSG), HttpStatus.OK);
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseMessageDto> activateAccount(@RequestBody @Valid RequestActivateAccountDto request) {
        // Todo: valid pincode not work
        registrationService.activateAccount(request.email(), request.pinCode());
        return new ResponseEntity<>(new ResponseMessageDto(HttpStatus.OK, ACCOUNT_ACTIVATE_MSG), HttpStatus.OK);
    }

    @GetMapping("/reactivate")
    public ResponseEntity<ResponseMessageDto> reActivateAccount(@RequestParam @Email String email) {
        registrationService.ReActivateAccount(email);
        return new ResponseEntity<>(new ResponseMessageDto(HttpStatus.OK, ACCOUNT_ACTIVATE_MSG), HttpStatus.OK);
    }

    @GetMapping("/resetpassword")
    public ResponseEntity<ResponseMessageDto> resetPassword(@RequestParam @Email String email) {
        registrationService.resetPassword(email);
        return new ResponseEntity<>(new ResponseMessageDto(HttpStatus.OK, ACCOUNT_RESET_PASSWORD_MSG), HttpStatus.OK);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<ResponseMessageDto> changePassword(@RequestBody @Valid RequestChangePasswordAccountDto request) {
        registrationService.changePassword(request.email(), request.pinCode(), request.password());
        return new ResponseEntity<>(new ResponseMessageDto(HttpStatus.OK, ACCOUNT_CHANGE_PASSWORD_MSG), HttpStatus.OK);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleException(BadCredentialsException ex) {
        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.UNAUTHORIZED,
                        "Bad credentials",
                        ex
                ), HttpStatus.UNAUTHORIZED);
    }
}
