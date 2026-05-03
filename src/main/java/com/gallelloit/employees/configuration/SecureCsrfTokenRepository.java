package com.gallelloit.employees.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Secure CSRF token repository that stores tokens in HttpOnly cookies
 * but makes them available to the frontend through request attributes.
 * This prevents XSS attacks while allowing frontend access to the token.
 */
public class SecureCsrfTokenRepository implements CsrfTokenRepository {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String CSRF_PARAM_NAME = "_csrf";

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAM_NAME, UUID.randomUUID().toString());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            // Remove existing cookie
            removeCookie(response);
        } else {
            // Save token in HttpOnly cookie for security
            String tokenValue = token.getToken();
            response.addHeader("Set-Cookie", 
                String.format("%s=%s; Path=/; HttpOnly; SameSite=Strict; Secure", 
                    CSRF_COOKIE_NAME, tokenValue));
            
            // Also make it available as request attribute for frontend frameworks
            request.setAttribute(CSRF_PARAM_NAME, token);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String tokenValue = getTokenFromCookie(request);
        if (!StringUtils.hasText(tokenValue)) {
            return null;
        }
        return new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAM_NAME, tokenValue);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        
        for (jakarta.servlet.http.Cookie cookie : cookies) {
            if (CSRF_COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void removeCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", 
            String.format("%s=; Path=/; HttpOnly; SameSite=Strict; Secure; Max-Age=0", 
                CSRF_COOKIE_NAME));
    }
}
