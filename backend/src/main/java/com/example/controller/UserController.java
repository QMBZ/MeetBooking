package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 获取所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 获取指定类型的用户
    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }

    // 通过用户名获取用户
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    // 通过 ID 获取用户名
    @GetMapping("/{userId}")
    public User getUserByUserId(@PathVariable Long userId) {
        return userService.getUserByUserId(userId);
    }

    // 根据角色类型添加用户
    @PostMapping("/{role}")
    public String addUser(@PathVariable Role role, @RequestBody User user) {
        userService.addUserByRole(user, role);
        return role.name().toLowerCase() + " user inserted successfully";
    }
}
