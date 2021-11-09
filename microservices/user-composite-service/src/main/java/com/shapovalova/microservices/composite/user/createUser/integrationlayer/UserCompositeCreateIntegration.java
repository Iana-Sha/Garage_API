package com.shapovalova.microservices.composite.user.createUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.user.User;
import com.shapovalova.api.core.user.UserCREATEServiceAPI;
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
public class UserCompositeCreateIntegration implements UserCREATEServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeCreateIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String userServiceUrl;

    public UserCompositeCreateIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                                          @Value("${app.user-service.host}") String userServiceHost,
                                          @Value("${app.user-service.port}") int userServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/user";

    }
    @Override
    public User createUser(User model) {
        try {
            String url = userServiceUrl;
            LOG.debug("Will call createUser API on URL: {}", url);
            return restTemplate.postForObject(url, model, User.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }

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
