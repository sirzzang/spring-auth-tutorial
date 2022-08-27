package com.eraser.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1. 스프링 시큐리티 UsernamePasswordAuuthenticationFilter는 /login 요청에서 username, password를 POST로 전송하면 동작
 * 2. FormLogin이 비활성화되면 작동하지 않음
 * 3. FormLogin 비활성화 상태에서 해당 필터가 동작하게 하고자 하면, addFilter를 통해 필터를 등록하면 됨
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // 로그인 진행을 위한 필터는 AuthenticationManager를 필요로 함
    private final AuthenticationManager authenticationManager;

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication: 로그인 시도");

        // 1. 로그인 시도 후 username, password 받음


        // 2. 정상인지 authenticationManager로 로그인 시도
        /**
         * 2.1. AuthenticationManager 로그인 시도 시, PrincipalDetailsService의 loadByUsername 자동 실행
         * 2.2. 실행 후 정상이면 PrincipalDetails 리턴됨
         */

        // 3. 리턴된 PrincipalDetails를 세션에 담음
        /**
         * 굳이 무상태 서버임에도 세션에 담는 이유는, 세션에 담지 않으면 시큐리티에서 권한 관리가 되지 않기 때문
         * 권한 관리 하지 않아도 되면, 세션에 담지 않아도 됨
         */

        // 4. JWT 토큰을 만들어서 응답하면 됨

        return super.attemptAuthentication(request, response);
    }
}
