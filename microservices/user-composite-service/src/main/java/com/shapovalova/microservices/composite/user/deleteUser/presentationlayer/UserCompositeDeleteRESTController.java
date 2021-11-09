package com.shapovalova.microservices.composite.user.deleteUser.presentationlayer;

import com.shapovalova.api.composite.user.UserCompositeDELETEServiceAPI;
import com.shapovalova.microservices.composite.user.deleteUser.businesslayer.UserCompositeDeleteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCompositeDeleteRESTController implements UserCompositeDELETEServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeDeleteRESTController.class);
    private final UserCompositeDeleteService userCompositeDeleteService;

    public UserCompositeDeleteRESTController(UserCompositeDeleteService userCompositeDeleteService) {
        this.userCompositeDeleteService = userCompositeDeleteService;
    }

    @Override
    public void deleteCompositeUser(int userId) {
        LOG.debug("UserComposite REST received deleteCompositeUser for userId: {}", userId);
        userCompositeDeleteService.deleteUser(userId);
    }
}
