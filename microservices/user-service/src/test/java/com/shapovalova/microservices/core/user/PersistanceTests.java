package com.shapovalova.microservices.core.user;

import com.shapovalova.microservices.core.user.datalayer.UserEntity;
import com.shapovalova.microservices.core.user.datalayer.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class PersistanceTests {
    @Autowired
    private UserRepository repository;
    private UserEntity savedEntity;

    @BeforeEach
    public void setupDb() {
        repository.deleteAll();
        //(int userId, String name, String lastName, String login, String password)
        UserEntity entity = new UserEntity(1, "n", "ln", "lg", "pwd");
        savedEntity = repository.save(entity);

        //expected, actual
        //assertEqualsUser(entity, savedEntity);

        //actual, expected
        assertThat(savedEntity, samePropertyValuesAs(entity));
    }

    @Test
    public void createUserEntity() {

        UserEntity newEntity = new UserEntity(2, "n","ln", "lg", "pwd");
        repository.save(newEntity);

        UserEntity foundEntity = repository.findById(newEntity.getId()).get();
        //assertEqualsUser(newEntity, foundEntity);

        assertThat(foundEntity, samePropertyValuesAs(newEntity));

        assertEquals(2, repository.count());
    }

    @Test
    public void updateUserEntity() {
        savedEntity.setName("n2");
        repository.save(savedEntity);

        UserEntity foundEntity = repository.findById(savedEntity.getId()).get();
        assertEquals(1, (long)foundEntity.getVersion());
        assertEquals("n2", foundEntity.getName());
    }

    @Test
    public void deleteUserEntity() {
        repository.delete(savedEntity);
        assertFalse(repository.existsById(savedEntity.getId()));
    }


    @Test
    public void getUserEntity() {
        Optional<UserEntity> entity = repository.findByUserId(savedEntity.getUserId());

        assertTrue(entity.isPresent());
        //assertEqualsUser(savedEntity, entity.get());

        assertThat(entity.get(), samePropertyValuesAs(savedEntity));

    }

    private void assertEqualsUser(UserEntity expectedEntity, UserEntity actualEntity) {
        assertEquals(expectedEntity.getId(),               actualEntity.getId());
        assertEquals(expectedEntity.getVersion(),          actualEntity.getVersion());
        assertEquals(expectedEntity.getUserId(),        actualEntity.getUserId());
        assertEquals(expectedEntity.getName(),           actualEntity.getName());
        assertEquals(expectedEntity.getLastName(),           actualEntity.getLastName());
        assertEquals(expectedEntity.getLogin(),           actualEntity.getLogin());
        assertEquals(expectedEntity.getPassword(),           actualEntity.getPassword());
    }
}
