package com.imaging.app.security;

import com.imaging.app.model.User;
import com.imaging.app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
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
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Value("${cookie.name}")
    private String COOKIE_NAME;

    @Value("${frontend-redirect-uri}")
    private String frontendRedirectUri;

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public OAuth2LoginSuccessHandler(
            UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("OAuth2 Authentication Success. Principal: {}", authentication.getPrincipal());

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        String email = oidcUser.getEmail();
        String userId = oidcUser.getSubject();

        if (email == null || userId == null) {
            log.error("Email or Subject (sub) claim missing in OIDC user info.");
            redirectStrategy.sendRedirect(
                    request, response, frontendRedirectUri + "?error=info_missing");
            return;
        }

        Optional<User> userOptional = userService.findById(userId);

        User user =
                userOptional.orElseGet(
                        () -> {
                            User newUser =
                                    User.builder()
                                            .userId(userId)
                                            .email(email)
                                            .name(oidcUser.getFullName())
                                            .picture(oidcUser.getPicture())
                                            .createdAt(LocalDateTime.now())
                                            .password(
                                                    passwordEncoder.encode(
                                                            UUID.randomUUID().toString()))
                                            .build();
                            return userService.createUser(newUser);
                        });

        String jwtToken = jwtUtil.generateToken(user.getUserId());

        log.info("Token created");

        Cookie cookie = new Cookie(COOKIE_NAME, jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(60 * 60);

        log.info("Cookie created");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        redirectStrategy.sendRedirect(request, response, frontendRedirectUri);
    }
}
