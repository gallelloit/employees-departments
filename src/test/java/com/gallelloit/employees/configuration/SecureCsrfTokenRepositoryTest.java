package com.gallelloit.employees.configuration;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.CsrfToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecureCsrfTokenRepositoryTest {

    private SecureCsrfTokenRepository repository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        repository = new SecureCsrfTokenRepository();
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        CsrfToken token = repository.generateToken(request);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertEquals("X-XSRF-TOKEN", token.getHeaderName());
        assertEquals("_csrf", token.getParameterName());
        assertFalse(token.getToken().isEmpty());
    }

    @Test
    void saveToken_shouldSetHttpOnlyCookieAndRequestAttribute() {
        CsrfToken token = new DefaultCsrfToken("X-XSRF-TOKEN", "_csrf", "test-token");

        repository.saveToken(token, request, response);

        // Verify cookie is set with HttpOnly flag
        verify(response).addHeader(eq("Set-Cookie"), contains("HttpOnly"));
        verify(response).addHeader(eq("Set-Cookie"), contains("XSRF-TOKEN=test-token"));
        verify(response).addHeader(eq("Set-Cookie"), contains("SameSite=Strict"));
        verify(response).addHeader(eq("Set-Cookie"), contains("Secure"));

        // Verify request attribute is set
        verify(request).setAttribute("_csrf", token);
    }

    @Test
    void saveToken_withNullToken_shouldRemoveCookie() {
        repository.saveToken(null, request, response);

        verify(response).addHeader(eq("Set-Cookie"), contains("Max-Age=0"));
        verify(request, never()).setAttribute(any(), any());
    }

    @Test
    void loadToken_withValidCookie_shouldReturnToken() {
        Cookie[] cookies = new Cookie[]{new Cookie("XSRF-TOKEN", "test-token")};
        when(request.getCookies()).thenReturn(cookies);

        CsrfToken token = repository.loadToken(request);

        assertNotNull(token);
        assertEquals("test-token", token.getToken());
        assertEquals("X-XSRF-TOKEN", token.getHeaderName());
        assertEquals("_csrf", token.getParameterName());
    }

    @Test
    void loadToken_withNoCookies_shouldReturnNull() {
        when(request.getCookies()).thenReturn(null);

        CsrfToken token = repository.loadToken(request);

        assertNull(token);
    }

    @Test
    void loadToken_withEmptyCookies_shouldReturnNull() {
        when(request.getCookies()).thenReturn(new Cookie[]{});

        CsrfToken token = repository.loadToken(request);

        assertNull(token);
    }

    @Test
    void loadToken_withWrongCookie_shouldReturnNull() {
        Cookie[] cookies = new Cookie[]{new Cookie("OTHER-COOKIE", "test-token")};
        when(request.getCookies()).thenReturn(cookies);

        CsrfToken token = repository.loadToken(request);

        assertNull(token);
    }

    @Test
    void loadToken_withEmptyToken_shouldReturnNull() {
        Cookie[] cookies = new Cookie[]{new Cookie("XSRF-TOKEN", "")};
        when(request.getCookies()).thenReturn(cookies);

        CsrfToken token = repository.loadToken(request);

        assertNull(token);
    }
}
