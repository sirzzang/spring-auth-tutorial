package com.eraser.jwt.config;

import com.eraser.jwt.filter.AuthTestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter /** TODO: deprecated 해결 */ {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: ThirdFilter 등록 시 FilterConfig에 있는 내용 무시됨
//        http.addFilter(new FirstFilter());
//        http.addFilterBefore(new ThirdFilter(), BasicAuthenticationFilter.class);
//        http.addFilterAfter(new ThirdFilter(), BasicAuthenticationFilter.class);

        // security filter 이전에 인증 필터 적용
        http.addFilterBefore(new AuthTestFilter(), SecurityContextPersistenceFilter.class);

        http.csrf().disable();

        // JWT 기본 설정(무상태 서버)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않도록 설정
                .and()

                // CORS 해제
                .addFilter(corsFilter) // @CrossOrigin(인증 x), 시큐리티 필터에 등록

                // 로그인 설정
                .formLogin().disable() // JWT 서버이므로 폼 태그를 이용한 로그인 설정 해제
                .httpBasic().disable() // HTTP Basic 로그인 방식 설정 해제
                .authorizeRequests()

                // API 엔드포인트별 권한 설정
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")

                // 권한 필요하지 않은 로그인
                .anyRequest().permitAll();

    }
}
