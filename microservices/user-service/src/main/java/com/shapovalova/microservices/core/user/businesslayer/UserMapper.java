package com.shapovalova.microservices.core.user.businesslayer;

import com.shapovalova.api.core.user.User;
import com.shapovalova.microservices.core.user.datalayer.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "serviceAddress", ignore = true)
    User entityToModel(UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    UserEntity modelToEntity(User model);
}
