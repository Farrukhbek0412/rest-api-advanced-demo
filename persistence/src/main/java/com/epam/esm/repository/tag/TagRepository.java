package com.epam.esm.repository.tag;


import com.epam.esm.entity.TagEntity;
import com.epam.esm.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends BaseRepository<TagEntity, Long> {

    int delete(Long id);

    Optional<TagEntity> findByName(String name);

    List<TagEntity> getMostWidelyUsedTagOfUser();

    Integer getCountOfMostWidelyUsedTagCount();
}
