package com.imaging.app.controller;

import com.imaging.app.exception.UserNotFoundException;
import com.imaging.app.mapper.UserMapper;
import com.imaging.app.model.User;
import com.imaging.app.security.AuthUtils;
import com.imaging.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUser() {
        String userId = AuthUtils.getAuthenticatedUserId();
        User user =
                userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return ResponseEntity.ok(UserMapper.toUserResponseDto(user));
    }
}
