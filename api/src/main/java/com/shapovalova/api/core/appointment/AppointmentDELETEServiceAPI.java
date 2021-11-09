package com.shapovalova.api.core.appointment;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface AppointmentDELETEServiceAPI {
    @DeleteMapping(value = "/appointment")
    void deleteAppointments(@RequestParam(value = "userId", required = true) int userId);
}
