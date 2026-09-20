package com.javarush.poroshina.taskManager.config;

import com.javarush.poroshina.taskManager.security.JwtAccessDeniedHandler;
import com.javarush.poroshina.taskManager.security.JwtAuthenticationEntryPoint;
import com.javarush.poroshina.taskManager.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint
            jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler
            jwtAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.jwtAuthenticationFilter =
                jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint =
                jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler =
                jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                jwtAccessDeniedHandler
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Регистрация и login — без JWT
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Health и scrape Prometheus — без JWT
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/prometheus"
                        )
                        .permitAll()

                        // Остальные Actuator endpoints —
                        // только ADMIN
                        .requestMatchers("/actuator/**")
                        .hasRole("ADMIN")

                        // Users — USER или ADMIN
                        .requestMatchers("/api/v1/users/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Удаление задач — только ADMIN
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/tasks/**"
                        )
                        .hasRole("ADMIN")

                        // Остальные задачи — USER или ADMIN
                        .requestMatchers("/api/v1/tasks/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Остальные запросы требуют авторизацию
                        .anyRequest()
                        .authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
