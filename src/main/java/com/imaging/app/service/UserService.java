package com.imaging.app.service;

import com.imaging.app.model.User;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);

    public boolean userExists(String userId);

    public Optional<User> findById(String userId);
}
