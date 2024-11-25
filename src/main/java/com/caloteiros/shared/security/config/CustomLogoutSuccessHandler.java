package com.caloteiros.shared.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final CacheManager cacheManager;

    public CustomLogoutSuccessHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            cacheManager.getCache("caloteiros").clear();
            cacheManager.getCache("caloteiros_listByUser").clear();
            cacheManager.getCache("caloteiros_search").clear();
        }

        response.sendRedirect(UriComponentsBuilder.fromPath("/auth/login").toUriString());
    }
}