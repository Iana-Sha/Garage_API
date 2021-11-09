package com.shapovalova.microservices.composite.user.getUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.car.CarGETServiceAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class CarGetIntegration implements CarGETServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(CarGetIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String carServiceUrl;

    public CarGetIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                             @Value("${app.car-service.host}") String carServiceHost,
                             @Value("${app.car-service.port}") int carServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        carServiceUrl = "http://" + carServiceHost + ":" + carServicePort + "/car";
    }

    @Override
    public List<Car> getCars(int userId) {
        try{
            String url = carServiceUrl +"?userId="+ userId;

            LOG.debug("Will call getCars API on URL: {}", url);
            List<Car> cars = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Car>>() {
                    }).getBody();

            LOG.debug("Found {} car for a user with id: {}", cars.size(), userId);
            return cars;
        }
        catch (Exception ex){
            LOG.warn("GOT an exception while requesting cars, return zero cars: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }


}
