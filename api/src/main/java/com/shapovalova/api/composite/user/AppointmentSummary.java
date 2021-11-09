package com.shapovalova.api.composite.user;

public class AppointmentSummary {
    private int appointmentId;
    private String date;
    private String time;

    public AppointmentSummary(int appointmentId, String date, String time) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
    }

    public AppointmentSummary() {

    }

    public int getAppointmentId() {
        return this.appointmentId;
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }



    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
