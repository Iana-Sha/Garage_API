package com.shapovalova.microservices.core.car.businesslayer;

import com.shapovalova.api.core.car.Car;

import java.util.List;

public interface CarService {
    public List<Car> getByUserId(int userId);

    public Car createCar(Car model);

    public void deleteCar(int userId);
}
