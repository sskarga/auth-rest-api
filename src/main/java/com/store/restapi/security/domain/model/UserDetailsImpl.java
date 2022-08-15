package com.store.restapi.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long userId;
    private String fullUserName;

    // Security
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean enabled;
    private boolean locked;

    public UserDetailsImpl(Account account) {
        this.userId = account.getId();
        this.fullUserName = account.getUsername();
        this.username = account.getEmail();
        this.password = account.getPassword();
        this.enabled = account.getEnabled();
        this.locked = account.getLocked();
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(account.getRole().getName()));
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullUserName() {
        return fullUserName;
    }

    // Security

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
        return !this.locked;
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
