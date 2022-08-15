package com.store.restapi.security.api.controller;

import com.store.restapi.security.api.facade.AccountFacade;
import com.store.restapi.security.api.payload.request.RequestSignUp;
import com.store.restapi.security.api.payload.response.ResponseAccount;
import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
@Slf4j
public class UserController {

    private final AccountService accountService;
    private final AccountFacade facade;

    @GetMapping
    public ResponseEntity<List<ResponseAccount>> getUser() {
        List<ResponseAccount> accounts = accountService.getUsers()
                .stream()
                .map(facade::toResponseAccount)
                .collect(Collectors.toList());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseAccount> getProfile(Authentication authentication) {
        Long id = ((UserDetailsImpl)authentication.getPrincipal()).getUserId();
        Account account = accountService.loadUserById(id).get();
        return new ResponseEntity<>(facade.toResponseAccount(account),HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.OK);
    }




}
