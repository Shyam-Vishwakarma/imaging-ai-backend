package com.imaging.app.service;

import com.imaging.app.model.User;
import com.imaging.app.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WalletService walletService;

    @Override
    public User createUser(User user) {
        walletService.createWallet(user.getUserId());
        return userRepository.save(user);
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
