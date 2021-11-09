package com.shapovalova.api.composite.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserCompositeDELETEServiceAPI {
    @DeleteMapping(value = "/user-composite/{userId}")
    void deleteCompositeUser(@PathVariable int userId);
}
