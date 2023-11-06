package com.mango.configuration;

import com.mango.jwt.JwtFilter;
import com.mango.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// spring security 설정 파일 입니다.
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
//    private final ConfigSuccessHandler configSuccessHandler;
//    private final ConfigFailureHandler configFailureHandler;

    private final String[] WHITE_LIST = {
            // swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            // oauth2
            "/oauth2/**",
            "/login/oauth2/**",
            // login
            "/api/user/login",
            "/api/user/join",
            // logout
            "/api/v1/logout",
            // websocket
            "/ws/**",
            "/message/**",
            "/room_name/**",
            // error
            "/error",
            //search
            "/api/search",
            "/api/search/**",
            //restaurant
            "/api/restaurant/**",
    };
//    private final String[] USER_ACCESS_LIST = {
//            "/api/v1/**"
//    };
//    private final String[] ADMIN_ACCESS_LIST = {
//            "/api/v1/admin/**"
//    };

    @Bean
    protected SecurityFilterChain myConfig(HttpSecurity http) throws Exception {
        /* 허용 페이지 등록 */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()  // 모든 사용자 허용 경로
//                        .requestMatchers(USER_ACCESS_LIST).hasRole("USER")  // USER 권한 필요 경로
//                        .requestMatchers(ADMIN_ACCESS_LIST).hasRole("ADMIN")  // ADMIN 권한 필요 경로
                        .anyRequest().authenticated())  // 그 외 나머지 경로는 전부 인증 필요
                // 예외 처리
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
                        })) // 권한 없음
                // 로그인
                // 로그아웃
//                .logout(logout -> logout
//                        .logoutUrl("/api/v1/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        http.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}