package com.shapovalova.microservices.core.car.businesslayer;

import com.shapovalova.api.core.car.Car;
import com.shapovalova.microservices.core.car.datalayer.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mappings(
            {
                    @Mapping(target = "serviceAddress", ignore = true)

            })
    Car entityToModel(CarEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true),

    })
    CarEntity modelToEntity(Car model);

    List<Car> entityListToModelList(List<CarEntity> entity);
    List<CarEntity> modelListToModelList(List<Car> model);
}
