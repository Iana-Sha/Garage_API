package com.shapovalova.api.composite.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserCompositeCREATEServiceAPI {
    @PostMapping(
            value = "/user-composite",
            consumes = "application/json"
    )
    void createCompositeUser(@RequestBody UserAggregate model);
}
