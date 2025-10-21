package com.example.chatserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        /// --- CÁC ĐƯỜNG DẪN CÔNG KHAI (PUBLIC) ---
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/login.html",
                                "/register.html",
                                "/css/**",  // <-- Sửa: Cho phép mọi thứ trong /css/
                                "/js/**",   // <-- Sửa: Cho phép mọi thứ trong /js/
                                "/img/**",  // (Thêm nếu bạn có thư mục ảnh)
                                "/ws/**"    // (Thêm nếu bạn cấu hình WebSocket endpoint)
                        ).permitAll()

                        // --- CÁC RULE XÁC THỰC (API) ---
                        // Rule CỤ THỂ phải đặt TRƯỚC rule CHUNG
                        .requestMatchers("/api/auth/introspect").authenticated()
                        .requestMatchers("/api/auth/**").permitAll() // Rule chung đặt sau

                        // --- CÁC ĐƯỜNG DẪN CÒN LẠI ---
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
