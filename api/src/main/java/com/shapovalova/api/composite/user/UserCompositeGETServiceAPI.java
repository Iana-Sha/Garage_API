package com.shapovalova.api.composite.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserCompositeGETServiceAPI {
    @GetMapping(
            value = "/user-composite/{userId}",
            produces = "application/json"
    )
    UserAggregate getUser(@PathVariable int userId);
}
