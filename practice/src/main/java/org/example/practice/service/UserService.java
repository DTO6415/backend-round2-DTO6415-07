package org.example.practice.service;

import org.example.practice.entity.User;
import org.example.practice.entity.UserRequest;
import org.example.practice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final JWTService jwtService;
    private final PasswordService passwordService;
    private final UserMapper userMapper;

    @Value("${spring.application.passwordMinLength}")
    private int passwordMinLength;
    @Value("${spring.application.usernameregex}")
    private String usernameregex;

    public UserService(JWTService jwtService, PasswordService passwordService, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }


    public Map<String, String> register(UserRequest userRequest) throws Exception {
        if(userRequest.getPassword().length() < passwordMinLength) {
            throw new IllegalArgumentException("密码长度不能少于 " + passwordMinLength + " 个字符");
        }
        if(!userRequest.getUsername().matches(usernameregex)) {
            throw new IllegalArgumentException("用户名格式不正确");
        }
        if(userMapper.findByUsername(userRequest.getUsername()) != null){
            throw new IllegalArgumentException("用户名已存在");
        }
        userMapper.insertUser(userRequest.getUsername(), passwordService.encode(userRequest.getPassword()));
        return Map.of("message", "注册成功", "newToken", jwtService.generateToken(userRequest.getUsername()));
    }


    private Map<String , String> isLoginValid(UserRequest userRequest){
        if (userMapper.findByUsername(userRequest.getUsername()) == null) return Map.of("message", "用户名不存在");
        if(!passwordService.matches(userRequest.getPassword(), userMapper.findByUsername(userRequest.getUsername()).getPassword())) return Map.of("message", "密码错误");
        return Map.of("isLoginValid", "登录成功","newToken", jwtService.generateToken(userRequest.getUsername()));
    }

    public Map<String, String> login(UserRequest userRequest) throws Exception {
        Map<String, String> result = new HashMap<>();
        if(userRequest.getToken() != null) {
            if (!jwtService.validateToken(userRequest.getToken())) {
                result.put("howToken", "登录凭证签名无效或已过期");
                result.putAll(isLoginValid(userRequest));
                return result;
            }
            if (!jwtService.getUsername(userRequest.getToken()).equals(userRequest.getUsername())) {
                result.put("howToken", "登录凭证与用户名不匹配");
                return result;
            }
            if (userMapper.findByUsername(jwtService.getUsername(userRequest.getToken())) == null) {
                result.put("howToken", "登录凭证对应的用户不存在");
                result.putAll(isLoginValid(userRequest));
                return result;
            }
                return Map.of("message", "登录成功", "newToken", jwtService.generateToken(userRequest.getUsername()));
        }
        else {
            result.put("howToken","未检测到登录凭证");
            result.putAll(isLoginValid(userRequest));
            return result;
        }
    }
    public User getinfo(String username) throws Exception {
        if(username == null) throw new Exception("用户名为空");
        User user = userMapper.findByUsername(username);
        if(user == null) throw new Exception("用户不存在");
        return user;
    }
}
