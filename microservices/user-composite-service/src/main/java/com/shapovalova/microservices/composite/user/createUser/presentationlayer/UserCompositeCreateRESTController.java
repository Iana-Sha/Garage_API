package com.shapovalova.microservices.composite.user.createUser.presentationlayer;

import com.shapovalova.api.composite.user.UserAggregate;
import com.shapovalova.api.composite.user.UserCompositeCREATEServiceAPI;
import com.shapovalova.microservices.composite.user.createUser.businesslayer.UserCompositeCreateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCompositeCreateRESTController implements UserCompositeCREATEServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeCreateRESTController.class);

    private final UserCompositeCreateService userCompositeCreateService;

    public UserCompositeCreateRESTController(UserCompositeCreateService userCompositeCreateService) {
        this.userCompositeCreateService = userCompositeCreateService;
    }

    @Override
    public void createCompositeUser(UserAggregate model) {
        LOG.debug("UserComposite REST received createCompositeUser");
        userCompositeCreateService.createUser(model);
    }
}
