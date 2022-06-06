package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GiftCertificateRepository
        extends BaseRepository<GiftCertificateEntity, Long>, GiftCertificateQueries {

    GiftCertificateEntity update(GiftCertificateEntity certificateUpdate);

    int delete(Long id);

    int updateDuration(int duration, Long id);

    int updatePrice(BigDecimal price, Long id);

    List<GiftCertificateEntity> getAllOnly(
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int pageSize,
            int pageNo
    );

    List<GiftCertificateEntity> getAllWithSearchAndTagName(
            String searchWord,
            Long tagId,
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int pageSize,
            int pageNo
    );

    List<GiftCertificateEntity> getAllWithSearch(
            String searchWord,
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int pageSize,
            int pageNo
    );


    List<GiftCertificateEntity> searchWithMultipleTags(
            List<TagEntity> tags,
            int pageSize,
            int pageNo
    );

    default String getSorting(boolean doNameSort, boolean doDateSort, boolean isDescending) {
        if (doNameSort && doDateSort && isDescending) {
            return ORDER_NAME_DATE_DESC;
        }
        if (doNameSort && doDateSort) {
            return ORDER_NAME_DATE;
        }
        if (doNameSort && isDescending) {
            return ORDER_NAME_DESC;
        }
        if (doNameSort) return ORDER_NAME;

        if (doDateSort && isDescending) {
            return ORDER_DATE_DESC;
        } else if (doDateSort) {
            return ORDER_DATE;
        } else
            return NO_ORDER;
    }
}
