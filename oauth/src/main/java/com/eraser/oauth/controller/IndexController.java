package com.eraser.oauth.controller;

import com.eraser.oauth.config.auth.PrincipalDetails;
import com.eraser.oauth.domain.User;
import com.eraser.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;


    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody PrincipalDetails user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return principalDetails;
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // SecurityConfig 파일 생성 후 기존 스프링 시큐리티 로그인 페이지는 작동하지 않음
    // 원래 스프링 시큐리티가 아래 주소를 낚아 챔
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    // 회원가입
    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");

        // 패스워드 암호화해야 시큐리티 로그인 가능
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user);

        return "redirect:/loginForm"; // 회원가입 완료 시 리다이렉트
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "personalInfo";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 허용되지 않은 유저일 경우 403
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "dataInfo";
    }

    @GetMapping("/login/test")
    public @ResponseBody String loginTest(
            Authentication authentication,
            @AuthenticationPrincipal PrincipalDetails userDetails) {

        /**
         * User를 찾는 방법
         * 1. Authentication 객체의 다운캐스팅
         * - 스프링 시큐리티 세션 정보에서 Authentication 객체에 접근
         * - Authentication 객체가 가지고 있는 PrincipalDetails 타입으로 User 정보에 접근
         * - PrincipalDetails가 가지고 있는 user 정보 접근
         * 2. @AuthenticationPrincipal 어노테이션
         * - @AuthenticationPrincipal 어노테이션을 통해 스프링 시큐리티 세션 정보에 접근
         * - 스프링 시큐리티 세션 정보를 PrincipalDetails로 형 변환하여 접근
         */
        log.info("/test/login=============");
        log.info("authentication: {}", authentication.getClass());

        // 1번 방식
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("authentication: {}", principalDetails.getUser());

        // 2번 방식
        log.info("userDetails: {}", userDetails.getUser());

        return "Spring security 세션 정보 확인하기";

    }

    @GetMapping("/login/test/oauth")
    public @ResponseBody String loginTestOauth(
            Authentication authentication,
            @AuthenticationPrincipal OAuth2User oauth) {

        /**
         * Oauth 로그인 시 User를 찾는 방법
         * 1. Authentication 객체 접근 후 OAuth2User 타입으로 다운캐스팅
         * 2. 스프링 세션 정보 접근 후 Oauth2User로 형 변환하여 접근
         */

        log.info("/test/login/oauth=============");
        log.info("oauth authentication: {}", authentication.getClass());
        log.info("oauth authentication type: {}", authentication);
        log.info("oauth authentication principal: {}", authentication.getPrincipal());

        // 1번 방식
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("oauth2User: {}", oAuth2User.getAttributes());

        // 2번 방식
        log.info("oauth: {}", oauth.getAttributes());

        return "Spring security oauth 세션 정보 확인하기";

    }

}
