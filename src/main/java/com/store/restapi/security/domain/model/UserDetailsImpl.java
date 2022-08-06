package com.store.restapi.security.domain.model;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Builder
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long userId;
    private String fullUserName;

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;

    public UserDetailsImpl(
            Long userId,
            String fullUserName,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled) {
        this.userId = userId;
        this.fullUserName = fullUserName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullUserName() {
        return fullUserName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
