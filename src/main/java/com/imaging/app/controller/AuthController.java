package com.imaging.app.controller;

import com.imaging.app.model.User;
import com.imaging.app.repository.UserRepository;
import com.imaging.app.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${allowed-origins}")
    private String frontendUrl;

    @Autowired
    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/oauth2/code/google")
    public ResponseEntity<?> googleCallback(
            @AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }

            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            String picture = principal.getAttribute("picture");
            String userId = principal.getName();

            User user =
                    User.builder()
                            .userId(userId)
                            .email(email)
                            .name(name)
                            .picture(picture)
                            .authProvider("google")
                            .build();

            if (!userRepository.existsById(userId)) {
                System.out.println("User not found, creating new user");
                userRepository.save(user);
            } else System.out.println("Existing user found");

            String token = jwtUtil.generateToken(userId, name);

            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            response.sendRedirect(frontendUrl + "/profile");
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication error: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(
            @CookieValue(name = "jwt_token", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        try {
            String userId = jwtUtil.extractUserId(token);
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) return ResponseEntity.ok(user.get());
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
