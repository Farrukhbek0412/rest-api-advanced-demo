package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.repository.CRUDRepository;

import java.util.Optional;

public interface UserRepository extends CRUDRepository<UserEntity, Long>, UserQueries{

    Optional<UserEntity> findByEmail(String email);
}
