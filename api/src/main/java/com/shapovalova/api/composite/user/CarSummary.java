package com.shapovalova.api.composite.user;

public class CarSummary {
    //private int userId;
    private int carId;
    private String vin;
    private String make;
    private String model;
    private String year;
    private String color;
    //private String serviceAddress;


    public CarSummary(int carId, String vin, String make, String model, String year, String color) {
        this.carId = carId;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    public CarSummary() {

    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
