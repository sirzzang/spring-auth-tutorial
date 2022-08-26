package com.eraser.jwt.filter;

import org.springframework.http.HttpMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class FirstFilter implements Filter {

    /**
     * Security 필터에 등록되지 않는 필터
     * Caused by: org.springframework.beans.BeanInstantiationException:
     * Failed to instantiate [javax.servlet.Filter]:
     * Factory method 'springSecurityFilterChain' threw exception;
     * nested exception is java.lang.IllegalArgumentException:
     * The Filter class com.eraser.jwt.filter.FirstFilter does not have a registered order and cannot be added without a specified order.
     * Consider using addFilterBefore or addFilterAfter instead.
     */


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        System.out.println("필터1끝");
    }
}
