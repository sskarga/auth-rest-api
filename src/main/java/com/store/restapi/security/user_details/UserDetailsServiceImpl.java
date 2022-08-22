package com.store.restapi.security.user_details;

import com.store.restapi.account.Account;
import com.store.restapi.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
