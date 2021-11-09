package com.shapovalova.api.core.car;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CarGETServiceAPI {
    @GetMapping(
            value = "/car",
            produces = "application/json"
    )
    List<Car> getCars(@RequestParam(value = "userId", required = true) int userId);
}
