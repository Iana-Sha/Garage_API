package com.shapovalova.microservices.composite.user.getUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.user.User;
import com.shapovalova.api.core.user.UserGETServiceAPI;
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
public class UserCompositeGetIntegration implements UserGETServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeGetIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String userServiceUrl;

    public UserCompositeGetIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                                       @Value("${app.user-service.host}") String userServiceHost,
                                       @Value("${app.user-service.port}") int userServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/user";
    }

    @Override
    public User getUser(int userId) {


        try{
            String url = userServiceUrl +"/"+ userId;
            LOG.debug("Will call getUser API on URL: {}", url);

            User user = restTemplate.getForObject(url, User.class);
            LOG.debug("Found a user with id: {}", user.getUserId());

            return user;
        }
        catch (HttpClientErrorException ex) { //since we are the API client we need to handle HTTP errors
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
