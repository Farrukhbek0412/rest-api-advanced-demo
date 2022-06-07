package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.repository.BaseRepository;

import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
