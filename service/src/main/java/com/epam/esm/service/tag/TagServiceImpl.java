package com.epam.esm.service.tag;

import com.epam.esm.dto.response.MostUsedTagResponse;
import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.repository.tag.TagRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public TagGetResponse create(TagPostRequest createTag) {

        TagEntity tagEntity = modelMapper.map(createTag, TagEntity.class);


        tagRepository.findByName(createTag.getName())
                .ifPresent((tag) -> {
                    throw new DataAlreadyExistException("This tag ( name: " +
                            tag.getName() + " ) already exists");
                });
        TagEntity createdTag = tagRepository.create(tagEntity);
        return modelMapper.map(createdTag, TagGetResponse.class);


    }

    @Override
    public TagGetResponse get(Long tagId) {
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        return Optional.ofNullable(modelMapper.map(tag.get(), TagGetResponse.class))
                .orElseThrow(() -> new DataNotFoundException("Tag ( id = " + tagId + " ) not found"));
    }

    @Override
    @Transactional
    public int delete(Long tagId) {

        int i = tagRepository.delete(tagId);
        if (i != 1)
            throw new DataNotFoundException("Tag ( id = " + tagId + " ) not found to delete");
        return i;
    }


    @Override
    public List<TagGetResponse> getAll(int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
        List<TagEntity> allTags = tagRepository.getAll(pageSize, pageNo);
        return modelMapper.map(allTags, new TypeToken<List<TagGetResponse>>() {
        }.getType());
    }

    @Override
    public MostUsedTagResponse getMostWidelyUsedTagsOfUser() {
        List<TagEntity> mostWidelyUserTagsOfUser = tagRepository.getMostWidelyUsedTagOfUser();
        BigInteger count = tagRepository.getCountOfMostWidelyUsedTagCount();
        if (mostWidelyUserTagsOfUser.isEmpty())
            throw new DataNotFoundException("this user haven't used any tags");
        MostUsedTagResponse mostUsedTagResponse = new MostUsedTagResponse();

        mostUsedTagResponse.setTags(
                modelMapper.map(mostWidelyUserTagsOfUser, new TypeToken<List<TagGetResponse>>() {
                }.getType()));
        mostUsedTagResponse.setCount(count);
        return mostUsedTagResponse;
    }

}