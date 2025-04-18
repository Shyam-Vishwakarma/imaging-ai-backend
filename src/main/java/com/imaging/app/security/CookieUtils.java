package com.imaging.app.security;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    @Value("${cookie.name}")
    private String COOKIE_NAME;

    @Value("${app.cookie.expiration}")
    private String expirationTime;

    public Cookie createCookie(String jwtToken) {
        Cookie cookie = new Cookie(COOKIE_NAME, jwtToken);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.parseInt(expirationTime));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public String extractJwt(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
