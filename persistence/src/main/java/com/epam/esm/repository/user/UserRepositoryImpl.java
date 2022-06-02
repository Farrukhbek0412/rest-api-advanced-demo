package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepositoryImpl implements UserRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity create(UserEntity userEntity) {
        entityManager.persist(userEntity);
        if(userEntity.getId() != null)
            return userEntity;
        return null;
    }

    @Override
    public List<UserEntity> getAll(int limit, int offset) {
        return entityManager.createQuery(GET_ALL, UserEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if(userEntity != null)
            return Optional.of(userEntity);
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        try {
            UserEntity userEntity = entityManager.createQuery(FIND_BY_EMAIL,
                            UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(userEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserEntity update(UserEntity obj) {
        return null;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }
}
