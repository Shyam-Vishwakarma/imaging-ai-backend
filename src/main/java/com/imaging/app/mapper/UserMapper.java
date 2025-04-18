package com.imaging.app.mapper;

import com.imaging.app.dto.UserResponseDto;
import com.imaging.app.model.User;
import java.time.LocalDateTime;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class UserMapper {
    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setName(user.getName());
        userResponseDto.setPicture(user.getPicture());
        return userResponseDto;
    }

    public static User toUser(OidcUser oidcUser) {
        return User.builder()
                .userId(oidcUser.getSubject())
                .email(oidcUser.getEmail())
                .name(oidcUser.getFullName())
                .picture(oidcUser.getPicture())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
