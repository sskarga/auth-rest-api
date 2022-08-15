package com.store.restapi.security.api.controller;

import com.store.restapi.advice.apiError.ApiError;
import com.store.restapi.security.api.facade.AccountFacade;
import com.store.restapi.security.api.payload.request.RequestSignIn;
import com.store.restapi.security.api.payload.request.RequestRefreshToken;
import com.store.restapi.security.api.payload.request.RequestSignUp;
import com.store.restapi.security.api.payload.response.ResponseAccount;
import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.dto.ResponseTokenDto;
import com.store.restapi.security.service.AccountService;
import com.store.restapi.security.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;
    private final AccountService accountService;
    private final AccountFacade facade;

    @PostMapping("/singin")
    public ResponseEntity<ResponseTokenDto> getAuthToken(@RequestBody @Valid RequestSignIn request) {
        ResponseTokenDto response = authService.signIn(request.username(), request.password());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/singup")
    public ResponseEntity<ResponseAccount> singUp(@RequestBody @Valid RequestSignUp request) {

        log.info("New account: {} - {}", request.email(), request.username());
        Account account = accountService.createUser(
                new Account(request.email(), request.password(), request.username())
        );
        return new ResponseEntity<>(facade.toResponseAccount(account),HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ResponseTokenDto> getRefreshToken(@RequestBody @Valid RequestRefreshToken request) {
        ResponseTokenDto response = authService.refresh(request.refreshToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody @Valid RequestRefreshToken request) {
        authService.logout(request.refreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
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
