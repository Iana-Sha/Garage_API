package com.shapovalova.microservices.composite.user.getUser.integrationlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.appointment.AppointmentGETServiceAPI;
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
public class AppointmentGetIntegration implements AppointmentGETServiceAPI {
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentGetIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;


    private final String appointmentServiceUrl;

    public AppointmentGetIntegration(RestTemplate restTemplate, ObjectMapper mapper,
                                     @Value("${app.appointment-service.host}") String appointmentServiceHost,
                                     @Value("${app.appointment-service.port}") int appointmentServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        appointmentServiceUrl = "http://" + appointmentServiceHost + ":" + appointmentServicePort + "/appointment";
    }

    @Override
    public List<Appointment> getAppointments(int userId) {


        try{

            String url = appointmentServiceUrl +"?userId="+ userId;

            LOG.debug("Will call getAppointment API on URL: {}", url);
            List<Appointment> appointments = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Appointment>>() {
                    }
            ).getBody();

            LOG.debug("Found {} appointments for a user with id: {}", appointments.size(), userId);
            return appointments;
        }
        catch(Exception ex) {
            LOG.warn("Got an exception while requesting appointments, return zero appointments: {}", ex.getMessage());
            return new ArrayList<>();
        }


    }
}
