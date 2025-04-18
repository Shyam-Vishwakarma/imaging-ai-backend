package com.imaging.app.security;

import com.imaging.app.mapper.UserMapper;
import com.imaging.app.model.User;
import com.imaging.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${frontend-redirect-uri}")
    private String frontendRedirectUri;

    private final CookieUtils cookieUtils;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("OAuth2 Authentication Success. Principal: {}", authentication.getPrincipal());

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        User user = UserMapper.toUser(oidcUser);

        String email = user.getEmail();
        String userId = user.getUserId();

        if (email == null || userId == null) {
            log.error("Email or Subject (sub) claim missing in OIDC user info.");
            redirectStrategy.sendRedirect(
                    request, response, frontendRedirectUri + "?error=info_missing");
            return;
        }

        if (!userService.userExists(userId)) {
            log.info("User not found in the database. Creating a new user.");
            user.setPassword(UUID.randomUUID().toString());
            user.setUpdatedAt(LocalDateTime.now());
            userService.createUser(user);
        }

        String jwtToken = jwtUtil.generateToken(user.getUserId());
        Cookie cookie = cookieUtils.createCookie(jwtToken);

        log.info("Cookie created");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        redirectStrategy.sendRedirect(request, response, frontendRedirectUri);
    }
}
