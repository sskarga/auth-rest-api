package com.store.restapi.security.service.impl;

import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetails load by username: {}", username);
        Account user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден: " + username));
        return new UserDetailsImpl(user);
    }

}
