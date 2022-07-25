package com.eraser.auth.userservice.service;

import com.eraser.auth.userservice.domain.Role;
import com.eraser.auth.userservice.domain.User;
import com.eraser.auth.userservice.repository.RoleRepository;
import com.eraser.auth.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // find user in user db
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User {} not found in the database.", username);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User {} found in the database.", username);
        }

        // add authorities from user roles
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database.", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword())); // encode user password
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database.", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        User user = userRepository.findByUsername(userName);
        Role role = roleRepository.findByName(roleName);

        log.info("Adding role {} to user {} database.", role.getName(), user.getName());
        user.getRoles().add(role);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String userName) {
        log.info("Fetching user {} from the database.", userName);
        return userRepository.findByUsername(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        log.info("Fetching all users from the database.");
        return userRepository.findAll();
    }


}
