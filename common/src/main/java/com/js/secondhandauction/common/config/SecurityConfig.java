package com.js.secondhandauction.common.config;

import com.js.secondhandauction.common.security.CustomAuthenticationProvider;
import com.js.secondhandauction.common.security.handler.CustomAccessDeniedHandler;
import com.js.secondhandauction.common.security.handler.CustomAuthenticationEntryPoint;
import com.js.secondhandauction.common.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //설정이유 : 로그인 이전 POST 요청을 허용하기 위해
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // ⭐️⭐️⭐️
                //.formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .userDetailsService(customUserDetailsService)
                .authenticationProvider(customAuthenticationProvider())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    // ⭐️ CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Arrays.asList("http://localhost", "http://alb-test-345516207.ap-northeast-2.elb.amazonaws.com"));
            config.setAllowCredentials(true);
            return config;
        };
    }

}
