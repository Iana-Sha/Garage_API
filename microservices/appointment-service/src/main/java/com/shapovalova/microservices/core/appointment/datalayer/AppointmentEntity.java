package com.shapovalova.microservices.core.appointment.datalayer;

import javax.persistence.*;

//@Document(collection = "appointments")
//@CompoundIndex(name = "appoint-rec-id", unique = true, def = "{'userId': 1, 'appointmentId' : 1}")
@Entity
@Table(name = "appointments", indexes = { @Index(name = "appointments_unique_idx",
        unique = true, columnList = "userId,appointmentId") })

public class AppointmentEntity {
    @Id
    @GeneratedValue
    private int id;
    @Version
    private int version;
    private int userId;
    private int appointmentId;
    private String date;
    private String time;

    public AppointmentEntity(){

    }

    public AppointmentEntity(int userId, int appointmentId, String date, String time) {
        this.userId = userId;
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
