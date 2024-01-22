package com.js.secondhandauction.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //TODO csrf 에 대해서 알아보기
                //설정이유 : 로그인 이전 POST 요청을 허용하기 위해
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        //.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .defaultSuccessUrl("/index.html", true)
                        .permitAll()
                )

                .logout(Customizer.withDefaults());
        return http.build();
    }
}
