package com.caloteiros.shared.security.config;

import com.caloteiros.shared.security.filter.SecurityFilter;
import com.caloteiros.shared.security.service.CachedUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CachedUserDetailsService cachedUserDetailsService;
    private final SecurityFilter securityFilter;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public SecurityConfig(CachedUserDetailsService cachedUserDetailsService, SecurityFilter securityFilter, CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.cachedUserDetailsService = cachedUserDetailsService;
        this.securityFilter = securityFilter;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(cachedUserDetailsService).passwordEncoder(passwordEncoder());

        http
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                        .requestMatchers("/auth/**","/login", "/auth/login", "/register", "/auth/register").permitAll()
                        .requestMatchers("/password/**", "/password/forgot", "/password/reset").permitAll()
                        .requestMatchers("/css/**", "/favicon", "/images/**", "/javascript/**", "/WEB-INF/tags/**", "/tags/**", "/WEB-INF/**","/views/includes/**", "/views/auth/**", "/views/users/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .permitAll()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/error");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}