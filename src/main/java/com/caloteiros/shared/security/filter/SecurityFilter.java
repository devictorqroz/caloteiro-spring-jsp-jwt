package com.caloteiros.shared.security.filter;

import com.caloteiros.shared.security.jwt.TokenService;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.repositories.UserRepository;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final List<String> publicRoutes = List.of(
            "/auth/**", "/login", "/register", "/password/**", "/api/auth/**",
            "/css/**", "/javascript/**", "/images/**", "/favicon.ico",
            "/swagger-ui/**", "/v3/api-docs/**", "/error"
    );

    public SecurityFilter(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();
        DispatcherType dispatcherType = request.getDispatcherType();
        logger.debug("Request URI: {} | Dispatcher: {}", uri, dispatcherType);


        if (dispatcherType == DispatcherType.FORWARD || dispatcherType == DispatcherType.ERROR) {
            logger.trace("Ignorando FORWARD/ERROR para {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        if (isPublicRoute(request.getRequestURI())) {
            logger.trace("Rota pública liberada: {}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        String tokenJWT = recoverToken(request);

        if (tokenJWT != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String login = tokenService.validateToken(tokenJWT);
            if (login != null) {
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("Usuário do token não encontrado no banco de dados"));

                var authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Autenticado via JWT: {}", login);
            } else {
                logger.warn("Token inválido recebido: {}", tokenJWT);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("JWT_TOKEN");
        }

        return null;
    }

    private boolean isPublicRoute(String uri) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return publicRoutes.stream().anyMatch(p -> pathMatcher.match(p, uri));
    }
}