package com.shapovalova.microservices.composite.user.createUser.businesslayer;

import com.shapovalova.api.composite.user.UserAggregate;

public interface UserCompositeCreateService {

    public void createUser (UserAggregate model);
}
