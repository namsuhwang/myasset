package com.idlelife.myasset.common.auth;

import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.service.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private long id;
    private String username;
    private String password;
    private List<String> roles;

    public CustomUserDetails(
            long id,
            String email,
            List<String> roles) {

        this.id = id;
        this.username = email;
        this.roles = roles;

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> roles = this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return roles;

    }

    // 계정 만료여부
    @Override
    public boolean isAccountNonExpired() {
        // true : 만료되지 않음
        // false : 만료됨
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        // true : 잠기지 않음
        // false : 잠김
        return true;
    }

    // 비밀번호 잠김 여부
    @Override
    public boolean isCredentialsNonExpired() {
        // true : 잠기지 않음
        // false : 잠김
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        // true : 활성화 상태
        // false : 비활성화 상태
        return true;
    }
}
