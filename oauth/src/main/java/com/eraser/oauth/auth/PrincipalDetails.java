package com.eraser.oauth.auth;

import com.eraser.oauth.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티의 로그인 진행 후 완료 시 시큐리티 세션이 생성
// 같은 세션 공간인데 시큐리티만의 세션 공간을 가져서 키 값으로 보관(Security ContextHolder)
// 시큐리티 세션 공간에 들어갈 수 있는 object는 Authentication 타입의 객체
// Authentication 타입 객체 안에 User 정보가 있어야 함
// User 정보는 UserDetails 타입 객체여야 함
// SecuritySession => Authentication => UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 해당 user의 권한 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
