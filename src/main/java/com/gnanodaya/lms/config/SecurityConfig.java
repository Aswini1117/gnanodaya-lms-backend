package com.gnanodaya.lms.config;

import com.gnanodaya.lms.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // ── ALL PUBLIC URLs (No Token Needed) ─────────────
    private static final String[] PUBLIC_URLS = {

            // ── Auth APIs ──────────────────────────────
            "/api/auth/**",

            // ── Swagger UI ─────────────────────────────
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF (not needed for stateless JWT)
                .csrf(csrf -> csrf.disable())

                // Stateless session (JWT based)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL Authorization Rules
                .authorizeHttpRequests(auth -> auth

                        // ── Public URLs (No Token) ──────────────
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // ── Super Admin Only ────────────────────
                        .requestMatchers("/api/super-admin/**")
                        .hasRole("SUPER_ADMIN")

                        // ── Admin + Super Admin ─────────────────
                        .requestMatchers("/api/admin/**")
                        .hasAnyRole("ADMIN", "SUPER_ADMIN")

                        // ── Instructor + Admin + Super Admin ────
                        .requestMatchers("/api/instructor/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN", "SUPER_ADMIN")

                        // ── Student + All Roles ──────────────────
                        .requestMatchers("/api/student/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMIN", "SUPER_ADMIN")

                        // ── All Other APIs Need Token ────────────
                        .anyRequest().authenticated()
                )

                // Add JWT Filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}