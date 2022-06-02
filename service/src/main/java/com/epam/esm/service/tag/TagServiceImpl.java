package com.epam.esm.service.tag;

import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.repository.tag.TagRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public TagGetResponse create(TagPostRequest createTag){
        TagEntity tagEntity = modelMapper.map(createTag, TagEntity.class);
        TagEntity createdTag = tagRepository.create(tagEntity);
        if(createdTag != null) {
            return modelMapper.map(createdTag, TagGetResponse.class);
        }
        throw new DataAlreadyExistException("This tag ( name: " + createdTag.getName() + " ) already exists");
    }

    @Override
    public TagGetResponse get(Long tagId) {
        Optional<TagEntity> tag = tagRepository.findById(tagId);
        if(tag.isPresent())
            return modelMapper.map(tag.get(), TagGetResponse.class);
        throw new DataNotFoundException("Tag ( id = " + tagId+ " ) not found");
    }

    @Override
    @Transactional
    public int delete(Long tagId) {

        int i= tagRepository.delete(tagId);
        if(i!=1)
            throw new DataNotFoundException("Tag ( id = "+tagId+" ) not found to delete");
        return i;
    }


    @Override
    public List<TagGetResponse> getAll(int limit, int offset) {
        List<TagEntity> allTags = tagRepository.getAll(limit, offset);
        return modelMapper.map(allTags, new TypeToken<List<TagGetResponse>>() {}.getType());
    }

    @Override
    public List<TagGetResponse> getMostWidelyUsedTagsOfUser(Long userId) {
        List<TagEntity> mostWidelyUserTagsOfUser = tagRepository.getMostWidelyUsedTagOfUser(userId);
        if(mostWidelyUserTagsOfUser.isEmpty())
            throw new DataNotFoundException("this user haven't used any tags");
        return modelMapper.map(mostWidelyUserTagsOfUser, new TypeToken<List<TagGetResponse>>() {}.getType());
    }
}