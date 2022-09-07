package com.store.restapi.account;

import com.store.restapi.security.user_details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/profile")
@Slf4j
public class AccountProfileController {
    private final AccountService accountService;
    private final AccountFacade facade;

    @GetMapping()
    public ResponseEntity<ResponseAccountDto> getProfile(Authentication authentication) {
        Long id = ((UserDetailsImpl)authentication.getPrincipal()).getUserId();

        Account account = accountService.findAccountById(id).orElseThrow(() -> new IllegalArgumentException("Not find profile user"));
        return new ResponseEntity<>(facade.toResponseAccount(account), HttpStatus.OK);
    }
}
