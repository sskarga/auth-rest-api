package com.store.restapi.security.auth;

import com.store.restapi.advice.api_error.ApiError;
import com.store.restapi.security.refresh_token.dto.RequestRefreshTokenDto;
import com.store.restapi.security.refresh_token.dto.ResponseTokenDto;
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
    private final AuthService authService;

    @PostMapping("/singin")
    public ResponseEntity<ResponseTokenDto> getAuthToken(@RequestBody @Valid RequestSignInDto request) {
        ResponseTokenDto response = authService.signIn(request.username(), request.password());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ResponseTokenDto> getRefreshToken(@RequestBody @Valid RequestRefreshTokenDto request) {
        ResponseTokenDto response = authService.refresh(request.refreshToken());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody @Valid RequestRefreshTokenDto request) {
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
