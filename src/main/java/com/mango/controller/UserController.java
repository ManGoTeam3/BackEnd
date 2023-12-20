package com.mango.controller;

import com.mango.dto.UserInfoDto;
import com.mango.dto.UserSignUpDto;
import com.mango.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://3.217.20.163:3000")
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 관련 API")
public class UserController{
    private final UserService userService;

    @Value("${jwt.expiration}")
    private Long expireTime;

    @Operation(summary = "회원가입")
    @PostMapping("/api/user/join")
    public ResponseEntity  signUp(@RequestBody UserSignUpDto userSignUpDto){
        userService.signUp(userSignUpDto.getUsername(), userSignUpDto.getPassword());
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "로그인")
    @PostMapping("/api/user/login")
    public ResponseEntity login(@RequestBody UserSignUpDto userSignUpDto) {
        String username = userSignUpDto.getUsername();
        String password = userSignUpDto.getPassword();
        String token = userService.login(username, password);
        UserInfoDto user = new UserInfoDto(username,token);
        return ResponseEntity.ok(user);
    }
}

