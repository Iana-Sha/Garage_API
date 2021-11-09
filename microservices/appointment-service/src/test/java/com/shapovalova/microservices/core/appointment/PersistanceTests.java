package com.shapovalova.microservices.core.appointment;

import com.shapovalova.microservices.core.appointment.datalayer.AppointmentEntity;
import com.shapovalova.microservices.core.appointment.datalayer.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class PersistanceTests {

    @Autowired
    AppointmentRepository repository;
    private AppointmentEntity savedEntity;

    @BeforeEach
    public void stupDb(){
        repository.deleteAll();

        AppointmentEntity entity = new AppointmentEntity(1,2, "a", "b");
        savedEntity = repository.save(entity);

        assertThat(savedEntity, samePropertyValuesAs(entity));
    }

    @Test
    public void createAppointment(){
        AppointmentEntity newEntity = new AppointmentEntity(1, 3, "a", "c");
        repository.save(newEntity);

        AppointmentEntity foundEntity = repository.findById(newEntity.getId()).get();

        assertThat(foundEntity, samePropertyValuesAs(newEntity));

        assertEquals(2, repository.count());
    }

    @Test
    public void updateAppointment() {
        savedEntity.setDate("a2");
        repository.save(savedEntity);

        AppointmentEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("a2", foundEntity.getDate());
    }

    @Test
    public void deleteAppointment() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    public void getAppointmentsByUserId() {
        List<AppointmentEntity> entityList = repository.findByUserId(savedEntity.getUserId());

        assertThat(entityList, hasSize(1));

        assertThat(entityList.get(0), samePropertyValuesAs(savedEntity));

    }

}
