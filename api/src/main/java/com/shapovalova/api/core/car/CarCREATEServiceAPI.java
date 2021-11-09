package com.shapovalova.api.core.car;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CarCREATEServiceAPI {
    @PostMapping(
            value = "/car",
            consumes = "application/json",
            produces = "application/json"
    )
    Car createCar(@RequestBody Car model);
}
