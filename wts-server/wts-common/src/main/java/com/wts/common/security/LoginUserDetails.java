package com.wts.common.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Spring Security 用户详情实现
 */
@Data
public class LoginUserDetails implements UserDetails {

    private String userId;
    private String loginName;
    private String name;
    private String password;
    private String userType;
    private Set<String> permissions;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return loginName;
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
        return true;
    }
}
