package com.example.demo.config;


import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

@AllArgsConstructor
@Configuration
@EnableWebSecurity //(debug = true)
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(basic -> basic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())//Csrf 안씀.
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// Session 안씀.
                .formLogin(login -> login.disable())
                .authorizeHttpRequests(authorize ->
                                authorize
                                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() // 제일 의심가는 부분. <- 해결
                                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                                        .anyRequest().permitAll()
                        // ADMIN페이지는 따로 시큐리티 Authority를 부여. <- 토큰 로그인이 X
                );
                //.addFilterBefore(new VerifyTokenFilter(customUserDetailsService,userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}