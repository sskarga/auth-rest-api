package com.store.restapi.account;

import com.store.restapi.account.ResponseAccountDto;
import com.store.restapi.account.AccountFacade;
import com.store.restapi.account.Account;
import com.store.restapi.security.user_details.UserDetailsImpl;
import com.store.restapi.account.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/account")
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final AccountFacade facade;

    @GetMapping
    public ResponseEntity<List<ResponseAccountDto>> getUser() {
        List<ResponseAccountDto> accounts = accountService.getAllAccounts()
                .stream()
                .map(facade::toResponseAccount)
                .collect(Collectors.toList());
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseAccountDto> getProfile(Authentication authentication) {
        Long id = ((UserDetailsImpl)authentication.getPrincipal()).getUserId();
        Account account = accountService.findAccountById(id).get();
        return new ResponseEntity<>(facade.toResponseAccount(account),HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.OK);
    }




}
