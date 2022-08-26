package com.eraser.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * CORS 설정이 필요할 때, {@link com.eraser.jwt.controller.RestApiController}에 {@code @CrossOrigin} 어노테이션을 사용해도 되나,
     * 위의 방식처럼 컨트롤러에 어노테이션을 적용하면 시큐리티 인증이 필요한 요청에 대해서는 거부됨.
     *
     * 인증이 필요한 요청의 경우, 필터를 타기 때문에, CORS 필터 클래스를 만들어서 인증 필터 타기 전에 걸어야 함
     * {@link SecurityConfig}에 필터로 등록 필요
     */

    @Bean
    public CorsFilter corsFilter() {

        System.out.println("cors 필터 시작");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 서버 응답 시 json을 자바스크립트에서 처리할 수 있게 설정
        config.addAllowedOrigin("*"); // 모든 ip에 응답 허용
        config.addAllowedHeader("*"); // 모든 헤더에 응답 허용
        config.addAllowedMethod("*"); // 모든 요청 방법에 대해 응답 허용

        // api로 들어오는 요청은 위의 설정을 따름
        source.registerCorsConfiguration("/api/**", config);

        System.out.println("cors 필터 끝");

        return new CorsFilter(source); // 스프링 필터에 해당 필터 등록 필요
    }
}
