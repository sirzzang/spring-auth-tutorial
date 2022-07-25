package com.eraser.auth.userservice.service;

import com.eraser.auth.userservice.domain.Role;
import com.eraser.auth.userservice.domain.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String userName, String roleName);
    User getUser(String userName);
    List<User> getUsers();
}
