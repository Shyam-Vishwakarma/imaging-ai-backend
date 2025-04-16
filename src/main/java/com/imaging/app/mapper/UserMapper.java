package com.imaging.app.mapper;

import com.imaging.app.dto.UserResponseDto;
import com.imaging.app.model.User;

public class UserMapper {
    public static UserResponseDto toUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setName(user.getName());
        userResponseDto.setPicture(user.getPicture());
        return userResponseDto;
    }
}
