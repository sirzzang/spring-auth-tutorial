package com.eraser.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthTestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        System.out.println("AuthTestFilter 시작");


        if (req.getMethod().equals("POST")) {
            System.out.println("POST request");

            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);

            if (headerAuth.equals("hello")) {
                System.out.println("인증 성공");
                chain.doFilter(req, res);

            } else {
                PrintWriter outPrintWriter = res.getWriter();
                System.out.println("인증 실패");
                outPrintWriter.println("인증 실패");
            }

            System.out.println("AuthTestFilter POST 요청 검증 끝");
        }

        System.out.println("AuthTestFilter 끝");
    }
}