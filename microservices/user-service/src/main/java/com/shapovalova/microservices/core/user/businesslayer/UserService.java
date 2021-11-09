package com.shapovalova.microservices.core.user.businesslayer;

import com.shapovalova.api.core.user.User;

public interface UserService {
    public User getUserById(int userId);

    public User createUser(User model);

    public void deleteUser(int userId);
}
