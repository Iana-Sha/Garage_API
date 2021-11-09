package com.shapovalova.microservices.core.appointment.businesslayer;

import com.shapovalova.api.core.appointment.Appointment;

import java.util.List;

public interface AppointmentService {
    public List<Appointment> getByUserId(int userId);

    public Appointment createAppointment(Appointment model);

    public void deleteAppointment(int userId);
}
