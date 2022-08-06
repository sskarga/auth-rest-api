package com.store.restapi.security.api.controller;

import com.store.restapi.security.api.dto.RequestAuth;
import com.store.restapi.security.dto.ResponseTokenDto;
import com.store.restapi.security.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@Slf4j
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("login")
    public ResponseEntity<ResponseTokenDto> getAuthToken(@RequestBody @Valid RequestAuth request) {
        log.info("Auth request {}", request);
        try {
            ResponseTokenDto response = authService.singUp(request.username(), request.password());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

//    @PostMapping("refresh")
//    public ResponseEntity<ResponseTokenDto> getRefreshToken(@RequestBody @Valid RequestRefreshToken request) {
//        DecodedJWT jwt = jwtProvider.verifyRefreshToken(request.refreshToken());
//
//    }
}
