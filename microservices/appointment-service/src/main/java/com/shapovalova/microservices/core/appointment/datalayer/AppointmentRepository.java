package com.shapovalova.microservices.core.appointment.datalayer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppointmentRepository  extends CrudRepository<AppointmentEntity, Integer> {
    @Transactional(readOnly = true)
    List<AppointmentEntity> findByUserId(int userId);
}
