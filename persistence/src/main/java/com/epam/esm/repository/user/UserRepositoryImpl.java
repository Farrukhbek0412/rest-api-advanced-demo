package com.epam.esm.repository.user;

import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.UnknownDataBaseException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity create(UserEntity userEntity) {
        entityManager.persist(userEntity);
        if (userEntity.getId() != null)
            return userEntity;
        throw new UnknownDataBaseException("There was a problem while creating user, please Try again!");
    }

    @Override
    public List<UserEntity> getAll(int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        query.select(root);
        return entityManager.createQuery(query)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if (userEntity != null)
            return Optional.of(userEntity);
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = builder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        query.select(root).where(builder
                .like(root.get("email"), "%" + email + "%"));

        try {
            UserEntity userEntity = entityManager.createQuery(query)
                    .getSingleResult();
            return Optional.of(userEntity);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
