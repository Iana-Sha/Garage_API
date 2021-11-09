package com.shapovalova.microservices.composite.user.createUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.appointment.AppointmentCREATEServiceAPI;
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
public class AppointmentCreateIntegration implements AppointmentCREATEServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentCreateIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;


    private final String appointmentServiceUrl;

    public AppointmentCreateIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                                        @Value("${app.appointment-service.host}") String appointmentServiceHost,
                                        @Value("${app.appointment-service.port}") int appointmentServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        appointmentServiceUrl = "http://" + appointmentServiceHost + ":" + appointmentServicePort + "/appointment";
    }

    @Override
    public Appointment createAppointment(Appointment model) {
        try {
            String url = appointmentServiceUrl;
            LOG.debug("Will call createAppointment API on URL: {}", url);

            Appointment appointment = restTemplate.postForObject(url, model, Appointment.class);
            LOG.debug("Created a appointment with userId: {} and appointmentId: {}", appointment.getUserId(), appointment.getAppointmentId());
            return appointment;
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
