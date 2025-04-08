package com.imaging.app.service;

import com.imaging.app.dto.UserDto;
import com.imaging.app.model.User;
import com.imaging.app.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(UserDto userDto) {
        System.out.println("Service:    " + userDto);
        User user =
                User.builder()
                        .userId(userDto.getUserId())
                        .email(userDto.getEmail())
                        .name(userDto.getName())
                        .picture(userDto.getPicture())
                        .authProvider(userDto.getAuthProvider())
                        .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
}
