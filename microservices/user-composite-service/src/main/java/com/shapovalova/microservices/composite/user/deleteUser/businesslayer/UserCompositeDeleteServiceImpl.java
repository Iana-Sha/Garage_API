package com.shapovalova.microservices.composite.user.deleteUser.businesslayer;

import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.AppointmentDeleteIntegration;
import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.CarDeleteIntegration;
import com.shapovalova.microservices.composite.user.deleteUser.integrationlayer.UserCompositeDeleteIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserCompositeDeleteServiceImpl implements UserCompositeDeleteService{
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeDeleteServiceImpl.class);

    private final AppointmentDeleteIntegration appointmentDeleteIntegration;
    private final CarDeleteIntegration carDeleteIntegration;
    private final UserCompositeDeleteIntegration userCompositeDeleteIntegration;

    public UserCompositeDeleteServiceImpl(AppointmentDeleteIntegration appointmentDeleteIntegration, CarDeleteIntegration carDeleteIntegration, UserCompositeDeleteIntegration userCompositeDeleteIntegration) {
        this.appointmentDeleteIntegration = appointmentDeleteIntegration;
        this.carDeleteIntegration = carDeleteIntegration;
        this.userCompositeDeleteIntegration = userCompositeDeleteIntegration;
    }

    @Override
    public void deleteUser(int userId) {
        LOG.debug("deleteCompositeUser: starting to delete a user aggregate for userId: {}", userId);

        userCompositeDeleteIntegration.deleteUser(userId);
        appointmentDeleteIntegration.deleteAppointments(userId);
        carDeleteIntegration.deleteCar(userId);

        LOG.debug("deleteCompositeUser: Deleted a user aggregate for userId: {}", userId);
    }
}
