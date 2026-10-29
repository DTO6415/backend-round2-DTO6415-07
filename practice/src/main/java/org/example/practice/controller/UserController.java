package org.example.practice.controller;

import jakarta.validation.Valid;
import org.example.practice.entity.User;
import org.example.practice.entity.UserRequest;
import org.example.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String,String> register(@Valid @RequestBody UserRequest userRequest) throws Exception {
        return userService.register(userRequest);
    }
    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody UserRequest userRequest) throws Exception {
        return userService.login(userRequest);
    }
    @GetMapping("/getinfo")
    public User getinfo(@RequestParam String username) throws Exception{
        return userService.getinfo(username);
    }
}
