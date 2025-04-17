package com.example.controller;

import com.example.constants.messages.UserMessage;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.model.Result;
import com.example.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "获取所有用户")
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return Result.ok("所有用户信息", userList);
    }

    @Operation(summary = "获取指定角色的用户")
    @GetMapping("/role/{role}")
    public Result<List<User>> getUsersByRole(@PathVariable Role role) {
        List<User> userList = userService.getUsersByRole(role);
        return Result.ok(role + " 用户信息", userList);
    }

    @Operation(summary = "通过用户名获取用户")
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return Result.create(HttpStatus.NOT_FOUND, "用户不存在", null);
        } else {
            return Result.ok("用户信息", user);
        }
    }

    @Operation(summary = "通过ID获取用户")
    @GetMapping("/{userId}")
    public Result<User> getUserByUserId(@PathVariable Long userId) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return Result.create(HttpStatus.NOT_FOUND, "用户不存在", null);
        } else {
            return Result.ok("用户信息", user);
        }
    }

    @Operation(summary = "根据角色类型创建用户")
    @PostMapping("/{role}")
    public Result<Boolean> createUser(@PathVariable Role role, @RequestBody User user) {
        HttpStatus createStatus = userService.addUserByRole(user, role);

        Map<HttpStatus, String> statusMessages = UserMessage.CREATE_MESSAGES;

        String message = statusMessages.getOrDefault(createStatus, "用户创建失败");

        return Result.create(createStatus, message, createStatus.is2xxSuccessful());
    }

    @Operation(summary = "查看用户状态，是否登陆")
    @GetMapping("/status")
    public Result<Boolean> getUserStatus(HttpSession session) {
        boolean isLoggedIn = userService.isUserLoggedIn(session);

        if (isLoggedIn) {
            return Result.ok("用户已登陆", Boolean.TRUE);
        } else {
            return Result.create(HttpStatus.UNAUTHORIZED, "用户未登录", Boolean.FALSE);
        }
    }

    @Operation(summary = "通过 session 获取当前用户信息")
    @GetMapping("/current")
    public Result<User> getUserBySession(HttpSession session) {
        User currentUser = userService.getUserBySession(session);

        if (currentUser == null) {
            return Result.create(HttpStatus.UNAUTHORIZED, "用户未登录", null);
        } else {
            return Result.ok("当前用户信息", currentUser);
        }
    }
}
