package com.mango.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDto {
    private String nickname;
    private String accessToken;

    @Builder
    public UserInfoDto(String nickname, String accessToken) {
        this.nickname = nickname;
        this.accessToken = accessToken;
    }
}
