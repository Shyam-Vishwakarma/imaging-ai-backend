package com.imaging.app.controller;

import com.imaging.app.dto.UserDto;
import com.imaging.app.mapper.UserMapper;
import com.imaging.app.model.User;
import com.imaging.app.security.JwtUtil;
import com.imaging.app.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${allowed-origins}")
    private String frontendUrl;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/oauth2/code/google")
    public ResponseEntity<?> googleCallback(
            @AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }

            UserDto userDto = new UserMapper().toUserDto(principal);
            System.out.println(userService.userExists(userDto.getUserId()));

            if (!userService.userExists(userDto.getUserId())) {
                userDto.setAuthProvider("google");
                userService.createUser(userDto);
            }

            String token = jwtUtil.generateToken(userDto.getUserId(), userDto.getName());

            Cookie cookie = new Cookie("imaging-jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            response.sendRedirect(frontendUrl + "/profile");
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during authentication");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(
            @CookieValue(name = "imaging-jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        try {
            String userId = jwtUtil.extractUserId(token);
            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) return ResponseEntity.ok(user.get());
            else return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("imaging-jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}
