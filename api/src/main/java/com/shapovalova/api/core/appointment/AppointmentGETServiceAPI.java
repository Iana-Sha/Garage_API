package com.shapovalova.api.core.appointment;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AppointmentGETServiceAPI {
    @GetMapping(
            value = "/appointment",
            produces = "application/json"
    )
    List<Appointment> getAppointments(@RequestParam(value = "userId", required = true) int userId);
}
