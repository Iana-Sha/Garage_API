package com.shapovalova.api.core.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserCREATEServiceAPI {
    @PostMapping(
            value = "/user",
            consumes = "application/json",
            produces = "application/json"
    )
    User createUser(@RequestBody User model);
}
