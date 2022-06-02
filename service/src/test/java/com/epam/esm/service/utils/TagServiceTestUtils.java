package com.epam.esm.service.utils;

import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.entity.TagEntity;

import java.util.ArrayList;
import java.util.List;

public class TagServiceTestUtils {
    public static List<TagEntity> getTagEntities() {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(1L);
        tagEntity.setName("java");

        TagEntity tagEntity1 = new TagEntity();
        tagEntity1.setId(2L);
        tagEntity1.setName("c++");

        TagEntity tagEntity2 = new TagEntity();
        tagEntity2.setId(3L);
        tagEntity2.setName("php");

        TagEntity tagEntity3 = new TagEntity();
        tagEntity3.setId(4L);
        tagEntity3.setName("pdp");

        TagEntity tagEntity4 = new TagEntity();
        tagEntity4.setId(5L);
        tagEntity4.setName("spring");

        List<TagEntity> tagEntities = new ArrayList<>();
        tagEntities.add(tagEntity);
        tagEntities.add(tagEntity1);
        tagEntities.add(tagEntity2);
        tagEntities.add(tagEntity3);
        tagEntities.add(tagEntity4);
        return tagEntities;
    }

    public static List<TagGetResponse> getTagGetResponses() {
        TagGetResponse tagEntity = new TagGetResponse();
        tagEntity.setId(1L);
        tagEntity.setName("java");

        TagGetResponse tagEntity1 = new TagGetResponse();
        tagEntity1.setId(2L);
        tagEntity1.setName("c++");

        TagGetResponse tagEntity2 = new TagGetResponse();
        tagEntity2.setId(3L);
        tagEntity2.setName("php");

        TagGetResponse tagEntity3 = new TagGetResponse();
        tagEntity3.setId(4L);
        tagEntity3.setName("pdp");

        TagGetResponse tagEntity4 = new TagGetResponse();
        tagEntity4.setId(5L);
        tagEntity4.setName("spring");

        List<TagGetResponse> tagGetResponses = new ArrayList<>();
        tagGetResponses.add(tagEntity);
        tagGetResponses.add(tagEntity1);
        tagGetResponses.add(tagEntity2);
        tagGetResponses.add(tagEntity3);
        tagGetResponses.add(tagEntity4);
        return tagGetResponses;
    }

    public static TagEntity getTestTagEntity() {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(1L);
        tagEntity.setName("java");
        return tagEntity;
    }

}
