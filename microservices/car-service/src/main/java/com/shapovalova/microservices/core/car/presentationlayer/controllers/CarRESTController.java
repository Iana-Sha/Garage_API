package com.shapovalova.microservices.core.car.presentationlayer.controllers;

import com.shapovalova.api.core.car.Car;
import com.shapovalova.api.core.car.CarCREATEServiceAPI;
import com.shapovalova.api.core.car.CarDELETEServiceAPI;
import com.shapovalova.api.core.car.CarGETServiceAPI;
import com.shapovalova.microservices.core.car.businesslayer.CarService;
import com.shapovalova.utils.exception.InvalidInputException;
import com.shapovalova.utils.exception.ReservedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CarRESTController implements CarCREATEServiceAPI, CarDELETEServiceAPI, CarGETServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(CarRESTController.class);

    //private final ServiceUtil serviceUtil;
    private final CarService carService;

    public CarRESTController(CarService carService){
        this.carService = carService;
    }

    @Override
    public List<Car> getCars(int userId) {
        if(userId < 1) throw new InvalidInputException("Invalid userId: " + userId);
        if (userId == 8) throw new ReservedException("Access denied. Private user found for Id: " + userId);
        List<Car> listCars = carService.getByUserId(userId);
        LOG.debug("/cars found response size: {}", listCars.size());
        return listCars;
    }

    @Override
    public Car createCar(Car model) {
        Car car = carService.createCar(model);
        LOG.debug("REST Controller createCar: created a car entity: {}/{}", car.getUserId(), car.getUserId());
        return car;
    }

    @Override
    public void deleteCar(int userId) {
        LOG.debug("REST Controller deleteCar: tries to delete car for the user with userId: {}", userId);
        carService.deleteCar(userId);
    }
}
