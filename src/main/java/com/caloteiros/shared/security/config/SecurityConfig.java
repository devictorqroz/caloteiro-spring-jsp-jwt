package com.caloteiros.shared.security.config;

import com.caloteiros.shared.security.filter.SecurityFilter;
import com.caloteiros.shared.security.service.CachedUserDetailsService;
import jakarta.servlet.DispatcherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/WEB-INF/views/**").permitAll()
                        .requestMatchers("/css/**", "/images/**", "/javascript/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/auth/**", "/login", "/register", "/password/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((req, res, ex) -> {
                            logger.warn("Acesso negado: {} | usuário não autenticado / redirecionado para login", req.getRequestURI());
                            res.sendRedirect(req.getContextPath() + "/auth/login");
                        })
                );

        logger.info("SecurityFilterChain configurado com sucesso.");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Configurando PasswordEncoder (BCrypt)");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        logger.debug("Registrando AuthenticationManager do Spring Security");
        return authenticationConfiguration.getAuthenticationManager();
    }
}