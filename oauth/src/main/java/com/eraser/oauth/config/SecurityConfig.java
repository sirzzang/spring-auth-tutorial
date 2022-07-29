package com.eraser.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured, preAuthorize, postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // TODO: deprecated adapter

    /**
     * PostAuthorize 어노테이션은 잘 안 씀
     * 하나의 메서드에만 걸고 싶으면 어노테이션으로 함
     * @return
     */

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                // 로그인 페이지 설정
                .loginPage("/loginForm")
                // 로그인 주소 호출(스프링 시큐리티가 낚아 채서 대신 로그인 진행)
                .loginProcessingUrl("/login")
                // loginForm 페이지에서 로그인 성공 시 메인 페이지로, 다른 페이지에서 로그인 시도 시 그 페이지로
                .defaultSuccessUrl("/");
    }

}
