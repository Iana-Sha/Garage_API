package com.shapovalova.microservices.composite.user.getUser.businesslayer;

import com.shapovalova.api.composite.user.AppointmentSummary;
import com.shapovalova.api.composite.user.CarSummary;
import com.shapovalova.api.composite.user.ServiceAddress;
import com.shapovalova.api.composite.user.UserAggregate;
import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.AppointmentGetIntegration;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.CarGetIntegration;
import com.shapovalova.microservices.composite.user.getUser.integrationlayer.UserCompositeGetIntegration;
import com.shapovalova.utils.exception.NotFoundException;
import com.shapovalova.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserCompositeGetServiceImpl implements UserCompositeGetService{
    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeGetServiceImpl.class);

    private final AppointmentGetIntegration appointmentGetIntegration;
    private final CarGetIntegration carGetIntegration;
    private final UserCompositeGetIntegration userCompositeGetIntegration;

    private final ServiceUtil serviceUtil;

    public UserCompositeGetServiceImpl(AppointmentGetIntegration appointmentGetIntegration, CarGetIntegration carGetIntegration, UserCompositeGetIntegration userCompositeGetIntegration, ServiceUtil serviceUtil) {
        this.appointmentGetIntegration = appointmentGetIntegration;
        this.carGetIntegration = carGetIntegration;
        this.userCompositeGetIntegration = userCompositeGetIntegration;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public UserAggregate getUser(int userId) {
        User user = userCompositeGetIntegration.getUser(userId);

        if(user == null) throw  new NotFoundException("No user found for userId: " + userId);

        List<Appointment> appointments = appointmentGetIntegration.getAppointments(userId);

        List<Car> cars = carGetIntegration.getCars(userId);

        return createUserAggregate(user, appointments, cars, serviceUtil.getServiceAddress());
    }
    private UserAggregate createUserAggregate(User user, List<Appointment> appointments, List<Car> cars, String serviceAddress) {
        // 1. Setup the user info
        int userId = user.getUserId();
        String name = user.getName();
        String lastName = user.getLastName();
        String login = user.getLogin();
        String password = user.getPassword();


        List<AppointmentSummary> appointmentSummaries = (appointments == null) ? null : appointments.stream()
                .map(r -> new AppointmentSummary(r.getAppointmentId(), r.getDate(), r.getTime()))
                .collect(Collectors.toList());
        List<CarSummary> carSummaries = (cars == null) ? null : cars.stream()
                .map(r -> new CarSummary(r.getCarId(), r.getVin(), r.getMake(), r.getModel(), r.getYear(), r.getColor()))
                .collect(Collectors.toList());

        String userAddress = user.getServiceAddress();
        String appointmentAddress = (appointments != null && appointments.size() > 0)
                ? appointments.get(0).getServiceAddress() : "";

        String carAddress = (cars != null & cars.size() > 0)
                ? cars.get(0).getServiceAddress() : "";
        ServiceAddress serviceAddresses = new ServiceAddress(serviceAddress, userAddress, carAddress,appointmentAddress);


        return new UserAggregate(userId, name, lastName, login, password, appointmentSummaries, carSummaries, serviceAddresses);
    }
}
