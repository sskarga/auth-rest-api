package com.store.restapi.security.service.impl;

import com.store.restapi.security.domain.model.User;
import com.store.restapi.security.domain.model.UserDetailsImpl;
import com.store.restapi.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с таким именем не найден: " + username));
        return toUserDetails(user);
    }

    private UserDetailsImpl toUserDetails(User user) {
        return UserDetailsImpl.builder()
                .username(user.getEmail()) // login
                .password(user.getPassword()) // password
                .enabled(user.getEnabled())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())))
                .build();
    }
}
