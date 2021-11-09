package com.shapovalova.microservices.composite.user.getUser.presentationlayer;

import com.shapovalova.api.composite.user.UserAggregate;
import com.shapovalova.api.composite.user.UserCompositeGETServiceAPI;
import com.shapovalova.microservices.composite.user.getUser.businesslayer.UserCompositeGetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCompositeGetRESTController implements UserCompositeGETServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeGetRESTController.class);

    private final UserCompositeGetService userCompositeGetService;

    public UserCompositeGetRESTController(UserCompositeGetService userCompositeGetService) {
        this.userCompositeGetService = userCompositeGetService;
    }


    @Override
    public UserAggregate getUser(int userId) {
        LOG.debug("UserComposite REST received getCompositeUser request for userId: {}", userId);
        UserAggregate userAggregate = userCompositeGetService.getUser(userId);
        return userAggregate;
    }
}
