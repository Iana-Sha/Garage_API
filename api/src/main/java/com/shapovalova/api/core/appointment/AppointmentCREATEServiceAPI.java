package com.shapovalova.api.core.appointment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AppointmentCREATEServiceAPI {
    @PostMapping(
            value = "/appointment",
            consumes = "application/json",
            produces = "application/json"
    )
    Appointment createAppointment(@RequestBody Appointment model);
}
