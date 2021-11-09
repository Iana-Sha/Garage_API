package com.shapovalova.api.core.appointment;

public class Appointment {
    private int userId;
    private int appointmentId;
    private String date;
    private String time;
    private String serviceAddress;

    public Appointment(int userId, int appointmentId, String date, String time, String serviceAddress) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.serviceAddress = serviceAddress;
    }

    public Appointment() {
        appointmentId = 0;
        userId = 0;
        date = null;
        time = null;
        serviceAddress = null;
    }

    public int getAppointmentId() {
        return this.appointmentId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getServiceAddress() {
        return this.serviceAddress;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
