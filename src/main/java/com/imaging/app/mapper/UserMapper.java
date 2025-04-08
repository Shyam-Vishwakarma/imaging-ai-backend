package com.imaging.app.mapper;

import com.imaging.app.dto.UserDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserMapper {

    public UserDto toUserDto(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");
        String userId = principal.getName();

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setPicture(picture);

        return userDto;
    }
}
