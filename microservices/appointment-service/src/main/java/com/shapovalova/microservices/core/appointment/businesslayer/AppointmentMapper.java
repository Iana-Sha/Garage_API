package com.shapovalova.microservices.core.appointment.businesslayer;

import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "serviceAddress", ignore = true)
    Appointment entityToModel(AppointmentEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    AppointmentEntity modelToEntity(Appointment model);

    List<Appointment> entityListToModelList(List<AppointmentEntity> entity);
    List<AppointmentEntity> modelListRoEntityList(List<Appointment> model);
}
