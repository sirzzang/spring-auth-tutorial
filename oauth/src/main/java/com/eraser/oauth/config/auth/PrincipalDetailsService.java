package com.eraser.oauth.config.auth;

import com.eraser.oauth.domain.User;
import com.eraser.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 스프링 시큐리티 설정에서 /login 요청에서 로그인이 처리되도록 하고,
// /login 요청이 오면 자동으로 UserDetailsService IoC된 loadUserByUserName 함수가 실행
// 규칙이므로 반드시 이렇게 만들어 주어야 함
// 로그인 시 사용자 이름을 username 필드로 만들어야 함. 그렇지 않으면 여기로 넘어와서 매칭이 되지 않음
// SecurityConfig에서 .userNameParameter 설정해주어야 함
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티 Session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null; // null 리턴 시 오류 발생
    }
}
