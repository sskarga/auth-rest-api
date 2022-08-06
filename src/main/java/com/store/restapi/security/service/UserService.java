package com.store.restapi.security.service;

import com.store.restapi.security.domain.model.Role;
import com.store.restapi.security.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getUsers();

    Optional<User> loadUserById(Long id);

    User saveUser(User user);

    User updateUser(User user);

    User deleteUser(Long id);

    Role saveRole(Role role);
    User addRoleToUser(String email, String roleName);
}
