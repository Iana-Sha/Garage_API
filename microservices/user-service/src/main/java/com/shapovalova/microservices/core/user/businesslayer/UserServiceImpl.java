package com.shapovalova.microservices.core.user.businesslayer;

import com.mongodb.DuplicateKeyException;
import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.core.user.datalayer.UserEntity;
import com.shapovalova.microservices.core.user.datalayer.UserRepository;
import com.shapovalova.utils.exception.InvalidInputException;
import com.shapovalova.utils.exception.NotFoundException;
import com.shapovalova.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    private final UserMapper mapper;

    private final ServiceUtil serviceUtils;

    public UserServiceImpl(UserRepository repository, UserMapper mapper, ServiceUtil serviceUtils) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtils = serviceUtils;
    }

    @Override
    public User getUserById(int userId) {
        UserEntity entity = repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));


        User response = mapper.entityToModel(entity);
        response.setServiceAddress(serviceUtils.getServiceAddress());

        LOG.debug("User getUserById: found userId: {}", response.getUserId());
        return response;
    }

    @Override
    public User createUser(User model) {
        try
        {
            UserEntity entity = mapper.modelToEntity(model);
            UserEntity newEntity = repository.save(entity);

            LOG.debug("createUser: entity created for userId: {}", model.getUserId());

            return mapper.entityToModel(newEntity);
        }
        catch (DuplicateKeyException dke)
        {
            throw new InvalidInputException("Duplicate key for userId: " + model.getUserId());
        }
    }

    @Override
    public void deleteUser(int userId) {
        LOG.debug("deleteUser: trying to delete entity with userId: {}", userId);
        repository.findByUserId(userId).ifPresent(e -> repository.delete(e));
    }
}
