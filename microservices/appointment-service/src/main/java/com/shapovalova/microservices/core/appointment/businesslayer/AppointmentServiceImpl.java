package com.shapovalova.microservices.core.appointment.businesslayer;

import com.shapovalova.api.core.appointment.Appointment;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentEntity;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentRepository;
import com.shapovalova.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository repository;

    private final AppointmentMapper mapper;

    private final ServiceUtil serviceUtils;

    public AppointmentServiceImpl(AppointmentRepository repository, AppointmentMapper mapper, ServiceUtil serviceUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtils = serviceUtils;
    }

    @Override
    public List<Appointment> getByUserId(int userId) {

        List<AppointmentEntity> entityList = repository.findByUserId(userId);
        List<Appointment> list = mapper.entityListToModelList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtils.getServiceAddress()));

        LOG.debug("Appointment getByUserId: response size: {}", list.size());
        return list;
    }

    @Override
    public Appointment createAppointment(Appointment model) {
        AppointmentEntity entity = mapper.modelToEntity(model);
        AppointmentEntity newEntity = repository.save(entity);

        LOG.debug("AppointmentService createAppointment: created a appointment entity: {}/{}", model.getUserId(), model.getAppointmentId());
        return mapper.entityToModel(newEntity);
    }

    @Override
    public void deleteAppointment(int userId) {
        LOG.debug("AppointmentService deleteAppointment: tries to delete all appointments for the user with userId: {}", userId);
        repository.deleteAll(repository.findByUserId(userId));
    }
}
