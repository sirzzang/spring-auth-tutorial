package com.eraser.oauth.config.auth;

import com.eraser.oauth.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    /**
     * Spring Security 세션에 보관될 정보
     * - SecuritySession => Authentication => UserDetails(PrincipalDetails)
     * - 스프링 시큐리티의 로그인 진행 후 완료 시 시큐리티 세션이 생성
     * - 같은 세션 공간인데 시큐리티만의 세션 공간을 가져서 키 값으로 보관(Security ContextHolder)
     * - 시큐리티 세션 공간에 들어갈 수 있는 object는 Authentication 타입의 객체
     * - Authentication 타입 객체 안에 User 정보가 있어야 함
     * - User 정보는 UserDetails 타입 객체여야 함 + Oauth 로그인할 경우, Oauth2User까지 같이
     */

    private User user; // userdetail
    private Map<String, Object> attributes; // oauth

    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // Oauth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user; // attributes 정보를 토대로 user 정보 만듦
        this.attributes = attributes;
    }


    /**
     * OAuth2User 타입 인증 정보
     */

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }


    @Override
    public String getName() {
        /**
         * oauth 로그인 시 sub가 인증 정보 primary key라서 그거 반환하면 되는데,
         * 별로 중요하지 않고 잘 쓰지도 않으므로 그냥 null 반환해 버리기
         */
        return null;
    }

    /**
     * UserDetails 타입 인증 정보
     */
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
