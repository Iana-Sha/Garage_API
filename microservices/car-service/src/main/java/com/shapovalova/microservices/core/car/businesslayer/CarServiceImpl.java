package com.shapovalova.microservices.core.car.businesslayer;

import com.shapovalova.api.core.car.Car;
import com.shapovalova.microservices.core.car.datalayer.CarEntity;
import com.shapovalova.microservices.core.car.datalayer.CarRepository;

import com.shapovalova.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService{

    private static final Logger LOG = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository repository;

    private final CarMapper mapper;

    private final ServiceUtil serviceUtil;

    public CarServiceImpl(CarRepository repository, CarMapper mapper, ServiceUtil serviceUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtils;
    }

    @Override
    public List<Car> getByUserId(int userId) {
        List<CarEntity> entityList = repository.findByUserId(userId);

        List<Car> list = mapper.entityListToModelList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));
        return list;
    }

    @Override
    public Car createCar(Car model) {
        CarEntity entity = mapper.modelToEntity(model);
        CarEntity newEntity = repository.save(entity);

        LOG.debug("CarService createCar: created a car entity: {}/{}", model.getUserId(), model.getCarId());

        return mapper.entityToModel(newEntity);
    }

    @Override
    public void deleteCar(int userId) {
        LOG.debug("CarService deleteCar: tries to delete all appointments for the user with userId: {}", userId);
        repository.deleteAll(repository.findByUserId(userId));
    }
}
