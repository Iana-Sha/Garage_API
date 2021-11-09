package com.shapovalova.api.core.car;

public class Car {
    private int userId;
    private int carId;
    private String vin;
    private String make;
    private String model;
    private String year;
    private String color;
    private String serviceAddress;

    public Car(int userId, int carId, String vin, String make, String model, String year, String color, String serviceAddress) {
        this.userId = userId;
        this.carId = carId;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.serviceAddress = serviceAddress;
    }

    public Car() {
        carId = 0;
        vin = null;
        make = null;
        model = null;
        year = null;
        color = null;
        userId = 0;
        serviceAddress = null;
    }

    public int getCarId() {
        return this.carId;
    }

    public String getVin() {
        return this.vin;
    }

    public String getMake() {
        return this.make;
    }

    public String getModel() {
        return this.model;
    }

    public String getYear() {
        return this.year;
    }

    public String getColor() {
        return this.color;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getServiceAddress() {
        return this.serviceAddress;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }
}
