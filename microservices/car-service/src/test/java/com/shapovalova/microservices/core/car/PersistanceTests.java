package com.shapovalova.microservices.core.car;

import com.shapovalova.microservices.core.car.datalayer.CarEntity;
import com.shapovalova.microservices.core.car.datalayer.CarRepository;
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
    private CarRepository repository;
    private CarEntity savedEntity;

    @BeforeEach
    public void setupDb(){
        repository.deleteAll();

        CarEntity entity = new CarEntity(1, 2, "123abc", "Toyota", "Corolla", "2008", "Grey");
        savedEntity = repository.save(entity);

        assertThat(savedEntity, samePropertyValuesAs(entity));
    }

    @Test
    public void create() {

        CarEntity newEntity = new CarEntity(1, 3, "123abc", "Toyota", "Corolla", "2008", "Grey");
        repository.save(newEntity);

        CarEntity foundEntity = repository.findById(newEntity.getId()).get();
        assertThat(foundEntity, samePropertyValuesAs(newEntity));

        assertEquals(2, repository.count());
    }

    @Test
    public void update() {
        savedEntity.setVin("a2");
        repository.save(savedEntity);

        CarEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("a2", foundEntity.getVin());
    }

    @Test
    public void delete() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }

    @Test
    public void getByUserId() {
        List<CarEntity> entityList = repository.findByUserId(savedEntity.getUserId());

        assertThat(entityList, hasSize(1));

        assertThat(entityList.get(0), samePropertyValuesAs(savedEntity));
    }

}
