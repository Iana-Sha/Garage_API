package com.shapovalova.api.composite.user;

public class ServiceAddress {
    private String compositeAddress;
    private String userAddress;
    private String carAddress;
    private String appointmentAddress;

    public ServiceAddress(String compositeAddress, String userAddress, String carAddress, String appointmentAddress) {
        this.compositeAddress = compositeAddress;
        this.userAddress = userAddress;
        this.carAddress = carAddress;
        this.appointmentAddress = appointmentAddress;
    }

    public ServiceAddress() {

    }

    public String getCompositeAddress() {
        return this.compositeAddress;
    }

    public String getUserAddress() {
        return this.userAddress;
    }

    public String getCarAddress() {
        return this.carAddress;
    }

    public String getAppointmentAddress() {
        return this.appointmentAddress;
    }

    public void setCompositeAddress(String compositeAddress) {
        this.compositeAddress = compositeAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setCarAddress(String carAddress) {
        this.carAddress = carAddress;
    }

    public void setAppointmentAddress(String appointmentAddress) {
        this.appointmentAddress = appointmentAddress;
    }
}
