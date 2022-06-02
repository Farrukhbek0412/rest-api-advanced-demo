package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GiftCertificateEntity create(GiftCertificateEntity certificate) {
        entityManager.persist(certificate);
        if(certificate.getId() != null)
            return certificate;
        return null;
    }

    @Override
    public List<GiftCertificateEntity> getAll(int limit, int offset) {
        return entityManager
                .createQuery(GET_ALL, GiftCertificateEntity.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificateEntity> findById(Long id) {
        GiftCertificateEntity certificateEntity = entityManager.find(GiftCertificateEntity.class, id);
        if(certificateEntity != null)
            return Optional.of(certificateEntity);
        return Optional.empty();
    }

    @Override
    public GiftCertificateEntity update(GiftCertificateEntity certificateUpdate) {
        return entityManager.merge(certificateUpdate);
    }

    @Override
    public int delete(Long id) {
        return entityManager
                .createQuery(DELETE)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public int updateDuration(int duration, Long id) {
        return entityManager.createNativeQuery(
                        UPDATE_DURATION)
                .setParameter("duration", duration)
                .setParameter("id", id)
                .setParameter("time", LocalDateTime.now())
                .executeUpdate();
    }


    @Override
    public List<GiftCertificateEntity> searchWithMultipleTags(List<TagEntity> tags, int limit, int offset) {
        return entityManager.createQuery(SEARCH_WITH_MULTIPLE_TAGS, GiftCertificateEntity.class)
                .setParameter("tags", tags)
                .setParameter("tagCount", (long) tags.size())
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> getAllWithSearchAndTagName(
            String searchWord, Long tagId, boolean doNameSort, boolean doDateSort,
            boolean isDescending, int limit, int offset
    ){
        String query = GET_ALL_WITH_SEARCH_AND_TAG_NAME + getSorting(doNameSort, doDateSort, isDescending);
            return entityManager.createNativeQuery(
                            query, GiftCertificateEntity.class)
                    .setParameter("searchWord", searchWord)
                    .setParameter("tagId", tagId)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> getAllWithSearch(
            String searchWord, boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset) {
        String query = GET_ALL_WITH_SEARCH + getSorting(doNameSort, doDateSort, isDescending);
        return entityManager.createNativeQuery(
                        query, GiftCertificateEntity.class)
                .setParameter("searchWord", searchWord)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> getAllOnly(boolean doNameSort, boolean doDateSort, boolean isDescending, int limit, int offset) {
        return entityManager
                .createNativeQuery(
                        GET_ALL + getSorting(doNameSort, doDateSort, isDescending),
                        GiftCertificateEntity.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }
}
