package com.mango.controller;

import com.mango.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://34.83.15.61:3000")
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 관련 API")
public class UserController{
    private final UserService userService;

//    @Value("${jwt.expiration}")
//    private Long expireTime;
//    @Operation(summary = "회원가입")
//    @PostMapping("/api/user/join")
//    public ResponseEntity join(@RequestBody UserSignUpDto userSignUpDto) {
//        userService.signUp(userSignUpDto.getEmail(), userSignUpDto.getPassword(), userSignUpDto.getNickName());
//        return ResponseEntity.ok().build();
//    }

//    @Operation(summary = "로그인")
//    @PostMapping("/api/user/login")
//    public ResponseEntity login(@RequestBody UserSignUpDto userSignUpDto) {
//        String token = userService.login(userSignUpDto.getEmail());
//        TokenDto tokenDto = new TokenDto();
//        tokenDto.setAccessToken(token);
//        tokenDto.setGrantType("Bearer");
//        tokenDto.setAccessTokenExpiresIn(String.valueOf(expireTime));
//        return ResponseEntity.ok(tokenDto);
//    }
}

