package com.eraser.oauth.controller;

import com.eraser.oauth.domain.User;
import com.eraser.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @GetMapping({"", "/"})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
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

}
