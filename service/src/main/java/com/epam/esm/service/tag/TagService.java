package com.epam.esm.service.tag;

import com.epam.esm.dto.response.MostUsedTagResponse;
import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService extends BaseService<TagPostRequest, TagGetResponse> {
    List<TagGetResponse> getAll(int limit, int offset);

    MostUsedTagResponse getMostWidelyUsedTagsOfUser();

}
