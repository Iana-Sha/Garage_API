package com.shapovalova.api.composite.user;

import java.util.List;

public class UserAggregate {
    private int userId;
    private String name;
    private String lastName;
    private String login;
    private String password;
    private List<AppointmentSummary> appointments;
    private List<CarSummary> cars;
    private ServiceAddress serviceAddresses;


    public UserAggregate(int userId, String name, String lastName, String login, String password, List<AppointmentSummary> appointments, List<CarSummary> cars, ServiceAddress serviceAddresses) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.appointments = appointments;
        this.cars = cars;
        this.serviceAddresses = serviceAddresses;
    }

    public UserAggregate() {

    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAppointments(List<AppointmentSummary> appointments) {
        this.appointments = appointments;
    }

    public void setCars(List<CarSummary> cars) {
        this.cars = cars;
    }

    public void setServiceAddresses(ServiceAddress serviceAddresses) {
        this.serviceAddresses = serviceAddresses;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<AppointmentSummary> getAppointments() {
        return this.appointments;
    }

    public List<CarSummary> getCars() {
        return this.cars;
    }

    public ServiceAddress getServiceAddresses() { return this.serviceAddresses; }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
