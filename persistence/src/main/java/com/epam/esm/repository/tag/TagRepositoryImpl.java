package com.epam.esm.repository.tag;

import com.epam.esm.entity.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


@Repository
public class TagRepositoryImpl implements TagRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TagEntity create(TagEntity tagEntity) {
        entityManager.persist(tagEntity);
        if (tagEntity.getId() != null)
            return tagEntity;
        return null;
    }

    @Override
    public List<TagEntity> getAll(int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> query = builder.createQuery(TagEntity.class);
        Root<TagEntity> root = query.from(TagEntity.class);
        query.select(root);

        return entityManager
                .createQuery(query)
                .setMaxResults(pageSize)
                .setFirstResult((pageNo - 1) * pageSize)
                .getResultList();
    }

    @Override
    public Optional<TagEntity> findById(Long id) {
        TagEntity tagEntity = entityManager.find(TagEntity.class, id);
        return Optional.ofNullable(tagEntity);
    }

    @Override
    public int delete(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<TagEntity> queryDelete = builder.createCriteriaDelete(TagEntity.class);
        Root<TagEntity> root = queryDelete.from(TagEntity.class);
        queryDelete.where(builder.equal(root.get("id"), id));
        return entityManager
                .createQuery(queryDelete)
                .executeUpdate();
    }

    @Override
    public Optional<TagEntity> findByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TagEntity> query = builder.createQuery(TagEntity.class);
        Root<TagEntity> root = query.from(TagEntity.class);
        query.select(root).where(builder.equal(root.get("name"), name));

        try {
            return Optional.of(entityManager.createQuery(query)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TagEntity> getMostWidelyUsedTagOfUser() {

        List resultList = entityManager.createStoredProcedureQuery(
                        "get_most_used_tags_of_a_user_which_has_a_highest_cost_order", TagEntity.class)
                .getResultList();
        return resultList;
    }

    @Override
    public Integer getCountOfMostWidelyUsedTagCount() {
        Integer a = (Integer) entityManager.createStoredProcedureQuery("get_count_of_most_used_tags")
                .getResultList().get(0);
        return a;
    }
}
