package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public GiftCertificateEntity create(GiftCertificateEntity certificate) {
        entityManager.persist(certificate);
        if (certificate.getId() != null)
            return certificate;
        return null;
    }

    @Override
    public List<GiftCertificateEntity> getAll(int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificateEntity> query = builder.createQuery(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> root = query.from(GiftCertificateEntity.class);
        query.select(root);

        return entityManager
                .createQuery(query)
                .setMaxResults(pageSize)
                .setFirstResult((pageNo - 1) * pageSize)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificateEntity> findById(Long id) {
        GiftCertificateEntity certificateEntity = entityManager.find(GiftCertificateEntity.class, id);
        return Optional.ofNullable(certificateEntity);
    }

    @Override
    public GiftCertificateEntity update(GiftCertificateEntity certificateUpdate) {
        return entityManager.merge(certificateUpdate);
    }

    @Override
    public int delete(Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<GiftCertificateEntity> queryDelete =
                builder.createCriteriaDelete(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> root = queryDelete.from(GiftCertificateEntity.class);
        queryDelete.where(builder.equal(root.get("id"), id));

        return entityManager
                .createQuery(queryDelete)
                .executeUpdate();
    }

    @Override
    public int updateDuration(int duration, Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<GiftCertificateEntity> query = builder.createCriteriaUpdate(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> root = query.from(GiftCertificateEntity.class);
        query.set(root.get("duration"), duration)
                .set(root.get("lastUpdateDate"), LocalDateTime.now())
                .where(builder.equal(root.get("id"), id));

        return entityManager.createQuery(query)
                .executeUpdate();
    }

    @Override
    public int updatePrice(BigDecimal price, Long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<GiftCertificateEntity> query = builder.createCriteriaUpdate(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> root = query.from(GiftCertificateEntity.class);
        query.set(root.get("price"), price)
                .set(root.get("lastUpdateDate"), LocalDateTime.now())
                .where(builder.equal(root.get("id"), id));
        return entityManager.createQuery(query)
                .executeUpdate();
    }

    @Override
    public List<GiftCertificateEntity> searchWithMultipleTags(List<TagEntity> tags, int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificateEntity> query = builder.createQuery(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> giftRoot = query.from(GiftCertificateEntity.class);
        Join<GiftCertificateEntity, TagEntity> tagJoin = giftRoot.join("tagEntities");

        CriteriaBuilder.In<String> inTagName = builder.in(tagJoin.get("name"));
        for (TagEntity tag : tags) {
            inTagName.value(tag.getName());
        }
        query.select(giftRoot).where(inTagName);
        query.groupBy(giftRoot.get("id"));
        query.having(builder.equal(builder.count(giftRoot), tags.size()));

        return entityManager.createQuery(query)
                .setMaxResults(pageSize)
                .setFirstResult((pageNo - 1) * pageSize)
                .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> getAllWithSearchAndTagName(
            String searchWord, Long tagId, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int pageSize, int pageNo
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificateEntity> query = builder.createQuery(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> giftRoot = query.from(GiftCertificateEntity.class);
        Join<GiftCertificateEntity, TagEntity> tags = giftRoot.join("tagEntities");

        query.where(builder.and(builder.equal(tags.get("id"), tagId)),
                builder.like(giftRoot.get("name"), "%" + searchWord + "%"));

        if (doNameSort) {
            query.orderBy(doNameSorting(builder, giftRoot, doNameSort, isDescending));
        }
        if (doDateSort) {
            query.orderBy(doDateSorting(builder, giftRoot, doDateSort, isDescending));
        }

        return entityManager.createQuery(query)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public Order doNameSorting(
            CriteriaBuilder builder,
            Root<GiftCertificateEntity> root,
            boolean doNameSort,
            boolean isDescending) {
        if (doNameSort && isDescending) {
            return builder.desc(root.get("name"));
        }
        return builder.asc(root.get("name"));

    }

    public Order doDateSorting(
            CriteriaBuilder builder,
            Root<GiftCertificateEntity> root,
            boolean doDateSort,
            boolean isDescending) {
        if (doDateSort && isDescending) {
            return builder.desc(root.get("createDate"));
        }
        return builder.asc(root.get("createDate"));
    }


    @Override
    public List<GiftCertificateEntity> getAllWithSearch(
            String searchWord, boolean doNameSort, boolean doDateSort, boolean isDescending, int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificateEntity> query = builder.createQuery(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> giftRoot = query.from(GiftCertificateEntity.class);

        query.where(
                builder.like(giftRoot.get("name"), "%" + searchWord + "%"));

        if (doNameSort) {
            query.orderBy(doNameSorting(builder, giftRoot, doNameSort, isDescending));
        }
        if (doDateSort) {
            query.orderBy(doDateSorting(builder, giftRoot, doDateSort, isDescending));
        }

        return entityManager.createQuery(query)
                .setFirstResult((pageNo - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> getAllOnly(boolean doNameSort, boolean doDateSort, boolean isDescending, int pageSize, int pageNo) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificateEntity> query = builder.createQuery(GiftCertificateEntity.class);
        Root<GiftCertificateEntity> giftRoot = query.from(GiftCertificateEntity.class);
        if (doNameSort) {
            query.orderBy(doNameSorting(builder, giftRoot, doNameSort, isDescending));
        }
        if (doDateSort) {
            query.orderBy(doDateSorting(builder, giftRoot, doDateSort, isDescending));
        }

        return entityManager
                .createQuery(query)
                .setMaxResults(pageSize)
                .setFirstResult((pageNo - 1) * pageSize)
                .getResultList();
    }
}
