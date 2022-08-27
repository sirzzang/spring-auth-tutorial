package com.eraser.jwt.config;

import com.eraser.jwt.filter.FirstFilter;
import com.eraser.jwt.filter.SecondFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

    /**
     * - Security Filter의 우선순위가 여기 등록되어 있는 필터의 우선순위보다 높음
     * - 만들어서 등록한 필터는 Security Filter Chain의 필터보다 더 늦게 실행됨
     * - 스프링 시큐리티의 필터보다 더 먼저 동작하게 하고 싶으면, {@link SecurityConfig}에서 시큐리티 필터 체인에서 더 먼저 있는 필터로 걸어 줘야 함
     */

    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilter() {
        FilterRegistrationBean<FirstFilter> bean = new FilterRegistrationBean<>(new FirstFilter());
        bean.addUrlPatterns("/*"); // 모든 URL 패턴에 대해서 전부 적용
//        bean.setOrder(0); // 낮은 번호가 필터 중 가장 먼저 실행됨
        return bean;
    }

    @Bean
    public FilterRegistrationBean<SecondFilter> secondFilter() {
        FilterRegistrationBean<SecondFilter> bean = new FilterRegistrationBean<>(new SecondFilter());
        bean.addUrlPatterns("/*");
//        bean.setOrder(1);
        return bean;
    }
}
