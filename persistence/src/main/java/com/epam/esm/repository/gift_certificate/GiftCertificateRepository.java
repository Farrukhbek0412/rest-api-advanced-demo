package com.epam.esm.repository.gift_certificate;

import com.epam.esm.entity.GiftCertificateEntity;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.CRUDRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepository
        extends CRUDRepository<GiftCertificateEntity, Long>, GiftCertificateQueries {

    int updateDuration(int duration, Long id);

    List<GiftCertificateEntity> getAllOnly(
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int limit,
            int offset
    );

    List<GiftCertificateEntity> getAllWithSearchAndTagName(
            String searchWord,
            Long tagId,
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int limit,
            int offset
    );

    List<GiftCertificateEntity> getAllWithSearch(
            String searchWord,
            boolean doNameSort,
            boolean doDateSort,
            boolean isDescending,
            int limit,
            int offset
    );


    List<GiftCertificateEntity> searchWithMultipleTags(
            List<TagEntity> tags,
            int limit,
            int offset
    );

    default String getSorting(boolean doNameSort, boolean doDateSort, boolean isDescending){
        if (doNameSort){
            if(doDateSort){
                if(isDescending)
                    return ORDER_NAME_DATE_DESC;
                return ORDER_NAME_DATE;
            }
            if(isDescending)
                return ORDER_NAME_DESC;
            return ORDER_NAME;
        }
        else if(doDateSort){
            if(isDescending){
                return ORDER_DATE_DESC;
            }
            return ORDER_DATE;
        }else
            return NO_ORDER;
    }
}
