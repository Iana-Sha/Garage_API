package com.shapovalova.api.core.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserDELETEServiceAPI {
    @DeleteMapping(
            value = "/user/{userId}"
    )
    void deleteUser(@PathVariable int userId);
}
