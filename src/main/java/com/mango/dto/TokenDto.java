package com.mango.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String accessTokenExpiresIn;
}
