package com.mango.service;

import com.mango.entity.User;
import com.mango.jwt.JwtTokenProvider;
import com.mango.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
//    public void signUp(String kakaoId, String nickname) {
//        userRepository.findByKakaoId(Long.valueOf(kakaoId))
//                .ifPresent(user -> {
//                    throw new ResponseStatusException(
//                            HttpStatus.BAD_REQUEST, "이미 존재하는 유저 이름입니다.");
//                });
//        User user = User.builder()
//                .kakaoId(Long.valueOf(kakaoId))
//                .name(nickname)
//                .build();
//        userRepository.save(user);
//    }

    public void serviceSignUp(String kakaoId) {
        userRepository.findByKakaoId(Long.valueOf(kakaoId))
                .ifPresent(user -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "이미 존재하는 유저 이름입니다.");
                });
        User user = User.builder()
                .kakaoId(Long.valueOf(kakaoId))
                .build();
        userRepository.save(user);
    }
    public String serviceLogin(String kakaoId) {
        if(userRepository.findByKakaoId(Long.valueOf(kakaoId)).isEmpty()){
            serviceSignUp(kakaoId);
        }
        User user = userRepository.findByKakaoId(Long.valueOf(kakaoId))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."));
        String token = jwtTokenProvider.createToken(String.valueOf(user.getKakaoId()));
        return token;
    }
}

