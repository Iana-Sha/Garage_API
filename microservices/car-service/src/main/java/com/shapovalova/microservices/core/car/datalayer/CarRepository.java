package com.shapovalova.microservices.core.car.datalayer;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CarRepository extends CrudRepository<CarEntity, Integer> {

    @Transactional(readOnly = true)
    List<CarEntity> findByUserId(int userId);
}
