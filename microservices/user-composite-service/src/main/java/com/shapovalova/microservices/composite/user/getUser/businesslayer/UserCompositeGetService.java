package com.shapovalova.microservices.composite.user.getUser.businesslayer;

import com.shapovalova.api.composite.user.UserAggregate;

public interface UserCompositeGetService {
    public UserAggregate getUser(int userId);
}
