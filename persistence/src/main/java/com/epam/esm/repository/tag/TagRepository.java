package com.epam.esm.repository.tag;


import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.CRUDRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface TagRepository extends CRUDRepository<TagEntity, Long>, TagQueries {
    TagEntity findByName(String name);

    List<TagEntity> getMostWidelyUsedTagOfUser();

    BigInteger getCountOfMostWidelyUsedTagCount();
}
