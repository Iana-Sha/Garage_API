package com.shapovalova.microservices.core.car.datalayer;

import javax.persistence.*;

@Entity
@Table(name = "cars", indexes = { @Index(name = "cars_unique_idx",
        unique = true, columnList = "userId,carId") })

public class CarEntity {

    @Id
    @GeneratedValue
    private int id;

    @Version
    private int version;
    private int userId;
    private int carId;
    private String vin;
    private String make;
    private String model;
    private String year;
    private String color;

    public CarEntity() {}

    public CarEntity(int userId, int carId, String vin, String make, String model, String year, String color){

        this.userId = userId;
        this.carId = carId;
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
