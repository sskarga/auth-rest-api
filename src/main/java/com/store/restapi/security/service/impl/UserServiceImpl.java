package com.store.restapi.security.service.impl;

import com.store.restapi.security.domain.model.Role;
import com.store.restapi.security.domain.model.User;
import com.store.restapi.security.domain.repository.RoleRepository;
import com.store.restapi.security.domain.repository.UserRepository;
import com.store.restapi.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final static String USER_NOT_FIND_MSG = "User with id %s not find.";

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> loadUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        User userOld = userRepo.findById(user.getId())
                .orElseThrow(() ->
                        new IllegalStateException(String.format(USER_NOT_FIND_MSG, user.getId().toString())));
        user.setCreatedOn(userOld.getCreatedOn());
        return this.saveUser(userOld);
    }

    @Override
    public User deleteUser(Long id) {
        User user = userRepo.findById(id)
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
    public User addRoleToUser(String email, String roleName) {
        log.info("Adding role {} to user {}", roleName, email);
        Role role = roleRepo.findByName(roleName);
        User user = userRepo.findByEmail(email).get();
        user.setRole(role);
        return userRepo.save(user);
    }
}
