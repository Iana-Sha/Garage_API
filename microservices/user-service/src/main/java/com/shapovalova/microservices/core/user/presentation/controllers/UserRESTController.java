package com.shapovalova.microservices.core.user.presentation.controllers;

import com.shapovalova.api.core.user.User;
import com.shapovalova.api.core.user.UserCREATEServiceAPI;
import com.shapovalova.api.core.user.UserDELETEServiceAPI;
import com.shapovalova.api.core.user.UserGETServiceAPI;
import com.shapovalova.microservices.core.user.businesslayer.UserService;
import com.shapovalova.utils.exception.InvalidInputException;
import com.shapovalova.utils.exception.ReservedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRESTController implements UserDELETEServiceAPI, UserCREATEServiceAPI, UserGETServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UserRESTController.class);

    private final UserService userService;

    public UserRESTController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User getUser(int userId) {

        LOG.debug("/user MS returns the found user for userId: " + userId);

        if (userId < 1) throw new InvalidInputException("Invalid userId: " + userId);

//        if (userId == 13) throw new NotFoundException("No user found for Id: " + userId);
//
        if (userId == 8) throw new ReservedException("Access denied. Private user found for Id: " + userId);

        User user = userService.getUserById(userId);
        return user;
    }

    @Override
    public User createUser(User model) {
        User user = userService.createUser(model);

        LOG.debug("REST createUser: user created for userId: {}", user.getUserId());

        return user;
    }

    @Override
    public void deleteUser(int userId) {
        LOG.debug("REST deleteUser:tried to delete userId: {}", userId);
        userService.deleteUser(userId);
    }
}
