package com.shapovalova.api.core.car;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface CarDELETEServiceAPI {
    @DeleteMapping(value = "/car")
    void deleteCar(@RequestParam(value = "userId", required = true) int userId);
}
