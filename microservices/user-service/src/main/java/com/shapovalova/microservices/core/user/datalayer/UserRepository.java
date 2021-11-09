package com.shapovalova.microservices.core.user.datalayer;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, String> {
    Optional<UserEntity> findByUserId(int userId);
}
