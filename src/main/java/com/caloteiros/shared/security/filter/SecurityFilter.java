package com.caloteiros.shared.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.caloteiros.shared.security.jwt.TokenService;
import com.caloteiros.user.domain.entities.User;
import com.caloteiros.user.domain.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (SecurityContextHolder .getContext().getAuthentication() == null) {

            String tokenJWT = null;
            User user = null;

            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
                tokenJWT = authHeader.substring(7);
            }

            if (tokenJWT == null) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    tokenJWT = (String) session.getAttribute("JWT_TOKEN");
                    user = (User) session.getAttribute("AUTHENTICATED_USER");
                }
            }

            if (tokenJWT != null) {

                try {
                    String login = tokenService.validateToken(tokenJWT);

                    if (user != null) {
                        user = userRepository.findByEmail(login)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    }

                    var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    HttpSession session = request.getSession();
                    if (session.getAttribute("AUTHENTICATED_USER") == null) {
                        session.setAttribute("AUTHENTICATED_USER", user);
                    }

                } catch (JWTVerificationException ex) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}


//HttpSession session = request.getSession(false);
//String token = (session != null) ? (String) session.getAttribute("JWT_TOKEN") : null;


//        if (token != null) {
//            var login = tokenService.validateToken(token);
//            if (login != null) {
//                User user = userRepository.findByEmail(login)
//                        .orElseThrow(() -> new RuntimeException("User not found"));
//
//                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
//                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }


//    private String recoverToken(HttpServletRequest request) {
//        var authHeader = request.getHeader("Authorization");
//        if (authHeader == null) return null;
//        return authHeader.replace("Bearer ", "");
//    }

