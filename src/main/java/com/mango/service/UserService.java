package com.mango.service;

import com.mango.dto.UserInfoDto;
import com.mango.entity.User;
import com.mango.jwt.JwtTokenProvider;
import com.mango.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

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

    public void serviceSignUp(String kakaoId, String nickname) {
        userRepository.findByKakaoId(Long.valueOf(kakaoId))
                .ifPresent(user -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "이미 존재하는 유저입니다.");
                });
        User user = User.builder()
                .name(nickname)
                .kakaoId(Long.valueOf(kakaoId))
                .build();
        userRepository.save(user);
        System.out.println("유저 생성 완료"+user.getKakaoId());
    }
    public UserInfoDto serviceLogin(Map<String,?> userInfo) {

        String id = userInfo.get("id").toString();
        Map<String , ?> kakaoAccount = (Map<String, ?>) userInfo.get("kakao_account");
        Map<String , String> profile = (Map<String, String>) kakaoAccount.get("profile");
        String name = profile.get("nickname");

        System.out.println("id : " + id);
        System.out.println("name : " + name);

//        System.out.println("profile : " + profile);

        if(userRepository.findByKakaoId(Long.valueOf(id)).isEmpty()){
            serviceSignUp(id,name);
        }
        User user = userRepository.findByKakaoId(Long.valueOf(id))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."));

        String token = jwtTokenProvider.createToken(String.valueOf(user.getKakaoId()));
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .accessToken(token)
                .nickname(name)
                .build();

        return userInfoDto;
    }
}

