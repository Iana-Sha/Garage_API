package com.shapovalova.microservices.composite.user.createUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.car.CarCREATEServiceAPI;
import com.shapovalova.utils.exception.InvalidInputException;
import com.shapovalova.utils.exception.NotFoundException;
import com.shapovalova.utils.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class CarCreateIntegration implements CarCREATEServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(CarCreateIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String carServiceUrl;

    public CarCreateIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                                @Value("${app.car-service.host}") String carServiceHost,
                                @Value("${app.car-service.port}") int carServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        carServiceUrl = "http://" + carServiceHost + ":" + carServicePort + "/car";
    }
    @Override
    public Car createCar(Car model) {
        try {
            String url = carServiceUrl;
            LOG.debug("Will call createCar API on URL: {}", url);
            Car car = restTemplate.postForObject(url, model, Car.class);
            LOG.debug("Created a car with userId: {} and userId: {}", car.getUserId(), car.getCarId());
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
        return null;
    }
    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {
            case NOT_FOUND:
                throw new NotFoundException(getErrorMessage(ex));
            case UNPROCESSABLE_ENTITY:
                throw new InvalidInputException(getErrorMessage(ex));
            default:
                LOG.warn("Got an unexpected HTTP error: {}, will rethrow it.", ex.getStatusCode());
                LOG.warn("Error body: {}", ex.getResponseBodyAsString());
                throw ex;
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {

        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex){
            return ex.getMessage();
        }

    }
}
