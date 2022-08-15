package com.store.restapi.security.service.impl;

import com.store.restapi.advice.exception.CustomApiException;
import com.store.restapi.security.domain.model.Role;
import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.domain.repository.RoleRepository;
import com.store.restapi.security.domain.repository.AccountRepository;
import com.store.restapi.security.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final static String USER_NOT_FIND_MSG = "User with id %s not find.";

    private final AccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<Account> loadUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<Account> loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Account createUser(Account user) {

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new CustomApiException("Account already");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role
        if (user.getRole() == null) {
            Role role = roleRepo.findByName("ROLE_USER");
            user.setRole(role);
        }

        return userRepo.save(user);
    }

    @Override
    public Account updateUser(Account user) {
        Account userOld = userRepo.findById(user.getId())
                .orElseThrow(() ->
                        new IllegalStateException(String.format(USER_NOT_FIND_MSG, user.getId().toString())));
        user.setCreatedOn(userOld.getCreatedOn());
        return userRepo.save(user);
    }

    @Override
    public Account deleteUser(Long id) {
        Account user = userRepo.findById(id)
                .orElseThrow(() ->
                        new IllegalStateException(String.format(USER_NOT_FIND_MSG, id.toString())));
        userRepo.delete(user);
        return user;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Save role {}",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public Account addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user {}", roleName, email);
        Role role = roleRepo.findByName(roleName);
        Account user = userRepo.findByEmail(email).get();
        user.setRole(role);
        return userRepo.save(user);
    }
}
