package com.eraser.jwt.config.auth;

import com.eraser.jwt.domain.User;
import com.eraser.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("PrincipalDetailsService.loadUserByUsername");
        User findUser = userRepository.findByUsername(username);
        return new PrincipalDetails(findUser);
    }
}
