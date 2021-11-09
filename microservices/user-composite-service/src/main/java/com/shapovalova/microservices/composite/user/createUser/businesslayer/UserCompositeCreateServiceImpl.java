package com.shapovalova.microservices.composite.user.createUser.businesslayer;

import com.shapovalova.api.composite.user.UserAggregate;
import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.AppointmentCreateIntegration;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.CarCreateIntegration;
import com.shapovalova.microservices.composite.user.createUser.integrationlayer.UserCompositeCreateIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserCompositeCreateServiceImpl implements UserCompositeCreateService{
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeCreateServiceImpl.class);

    private final UserCompositeCreateIntegration userCompositeCreateIntegration;

    private final CarCreateIntegration carCreateIntegration;

    private final AppointmentCreateIntegration appointmentCreateIntegration;

    public UserCompositeCreateServiceImpl(UserCompositeCreateIntegration userCompositeCreateIntegration, CarCreateIntegration carCreateIntegration, AppointmentCreateIntegration appointmentCreateIntegration) {
        this.userCompositeCreateIntegration = userCompositeCreateIntegration;
        this.carCreateIntegration = carCreateIntegration;
        this.appointmentCreateIntegration = appointmentCreateIntegration;
    }

    @Override
    public void createUser(UserAggregate model) {
        try {
            LOG.debug("createCompositeUser: creates a new composite entity for userId: {}", model.getUserId());

            User user = new User(model.getUserId(), model.getName(), model.getLastName(), model.getLogin(), model.getPassword(), null);
            userCompositeCreateIntegration.createUser(user);


            if(model.getAppointments() != null)
            {
                model.getAppointments().forEach(r -> {
                    Appointment appointment = new Appointment(model.getUserId(), r.getAppointmentId(), r.getDate(),
                            r.getTime(), null);
                    appointmentCreateIntegration.createAppointment(appointment);
                });
            }
            if(model.getCars() != null)
            {
                model.getCars().forEach(r -> {
                    Car car = new Car(model.getUserId(), r.getCarId(), r.getVin(), r.getMake(), r.getModel(), r.getYear(), r.getColor(), null);
                    carCreateIntegration.createCar(car);
                });
            }
            LOG.debug("createCompositeUser: creates a new composite entity for userId: {}", model.getUserId());

        }
        catch (RuntimeException ex){
            LOG.warn("createCompositeUser failed",ex);
            throw ex;
        }

    }

}
