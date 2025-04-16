package com.imaging.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AuthEntryPoint authEntryPoint;

    @Value("${cookie.name}")
    private String COOKIE_NAME;

    public JWTAuthenticationFilter(
            JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthEntryPoint authEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String userId = null;
            String jwt = null;
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (COOKIE_NAME.equals(cookie.getName())) {
                        log.info("JWT found in cookie: {}", cookie.getValue());
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }

            if (jwt != null) {
                userId = jwtUtil.extractUserId(jwt);
            }

            if (userId != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                if (jwtUtil.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            authEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            authEntryPoint.commence(
                    request, response, new BadCredentialsException("Invalid auth", e));
        }
    }
}
