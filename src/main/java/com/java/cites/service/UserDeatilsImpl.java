package com.java.cites.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.cites.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
public class UserDeatilsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    public UserDeatilsImpl(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public static UserDeatilsImpl build(User user) {

        return new UserDeatilsImpl(
                user.getUsername(),
                user.getEmail(),
                user.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDeatilsImpl user = (UserDeatilsImpl) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
