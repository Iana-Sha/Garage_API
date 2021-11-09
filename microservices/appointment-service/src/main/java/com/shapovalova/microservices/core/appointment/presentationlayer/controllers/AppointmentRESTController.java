package com.shapovalova.microservices.core.appointment.presentationlayer.controllers;

import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.appointment.AppointmentCREATEServiceAPI;
import com.shapovalova.api.core.appointment.AppointmentDELETEServiceAPI;
import com.shapovalova.api.core.appointment.AppointmentGETServiceAPI;
import com.shapovalova.microservices.core.appointment.businesslayer.AppointmentService;
import com.shapovalova.utils.exception.InvalidInputException;

import com.shapovalova.utils.exception.ReservedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppointmentRESTController implements AppointmentDELETEServiceAPI, AppointmentGETServiceAPI, AppointmentCREATEServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentRESTController.class);

    private final AppointmentService appointmentService;

    public AppointmentRESTController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public List<Appointment> getAppointments(int userId) {
        if(userId < 1) throw new InvalidInputException("Invalid userId: " + userId);

        if (userId == 8) throw new ReservedException("Access denied. Private user found for Id: " + userId);
//
//        if(userId == 113){
//            LOG.debug("No appointment found for userID: {}" + userId);
//            return new ArrayList<>();
//        }
//
        List <Appointment> listAppointment = appointmentService.getByUserId(userId);
//
//        //public Appointment(int userId, int appointmentId, String date, String time, int carId, String serviceAddress)
//
//        listAppointment.add(new Appointment(userId, 1, "2021-04-17", "8AM", 1, serviceUtil.getServiceAddress()));
//        listAppointment.add(new Appointment(userId, 2, "2021-05-17", "8AM", 2, serviceUtil.getServiceAddress()));
//        listAppointment.add(new Appointment(userId, 3, "2021-06-17", "8AM", 3, serviceUtil.getServiceAddress()));

        LOG.debug("appointments found response size: {}", listAppointment.size());

        return listAppointment;
    }

    @Override
    public Appointment createAppointment(Appointment model) {
        Appointment appointment = appointmentService.createAppointment(model);

        LOG.debug("REST Controller createAppointment: created a appointment entity: {}/{}", appointment.getUserId(), appointment.getUserId());
        return appointment;
    }

    @Override
    public void deleteAppointments(int userId) {
        LOG.debug("REST Controller deleteAppointments: tries to delete appointments for the user with userId: {}", userId);
        appointmentService.deleteAppointment(userId);
    }
}
