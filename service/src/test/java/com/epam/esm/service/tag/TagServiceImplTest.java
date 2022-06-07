package com.epam.esm.service.tag;

import com.epam.esm.dto.response.MostUsedTagResponse;
import com.epam.esm.dto.response.TagGetResponse;
import com.epam.esm.dto.request.TagPostRequest;
import com.epam.esm.entity.TagEntity;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.repository.tag.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.utils.TagServiceTestUtils.getTagEntities;
import static com.epam.esm.service.utils.TagServiceTestUtils.getTagGetResponses;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private TagEntity tag;
    private TagGetResponse tagGetResponse;
    private TagPostRequest tagPostRequest;

    @BeforeEach
    public void setUp() {
        tag = new TagEntity();
        tag.setName("test tag");

        tagGetResponse = new TagGetResponse();
        tagGetResponse.setName("java");

        tagPostRequest = new TagPostRequest();
        tagPostRequest.setName("test post tag ");
    }

    @Test
    void canCreateTagResultTest() {
        when(tagRepository.create(tag)).thenReturn(tag);
        when(modelMapper.map(tag, TagGetResponse.class)).thenReturn(tagGetResponse);
        when(modelMapper.map(tagPostRequest, TagEntity.class)).thenReturn(tag);

        TagGetResponse response = tagService.create(tagPostRequest);

        assertNotNull(response);
    }

    @Test
    void canGetTagByIdResultTest() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(modelMapper.map(tag, TagGetResponse.class)).thenReturn(tagGetResponse);

        TagGetResponse response = tagService.get(1L);

        assertEquals(tagGetResponse, response);
    }

    @Test
    void canGetAllResultTest() {
        List<TagEntity> tagEntities = getTagEntities();
        List<TagGetResponse> tagGetResponses = getTagGetResponses();
        when(tagRepository.getAll(5, 0)).thenReturn(tagEntities);
        when(modelMapper.map(tagEntities, new TypeToken<List<TagGetResponse>>() {
        }.getType()))
                .thenReturn(tagGetResponses);

        List<TagGetResponse> all = tagService.getAll(5, 0);

        assertEquals(5, all.size());
    }

    @Test
    void canDeleteByIdResultTest() {
        when(tagRepository.delete(1L)).thenReturn(1);
        int delete = tagRepository.delete(1L);
        assertEquals(1, delete);
    }

    @Test
    public void testDeleteTagThrowsException() {
        when(tagRepository.delete(2L)).thenReturn(0);

        assertThrows(DataNotFoundException.class, () -> tagService.delete(2L));
        verify(tagRepository, times(1)).delete(2L);
    }

    @Test
    void canGetMostWidelyUsedTagsOfUserTest() {
        List<TagEntity> tagEntities = getTagEntities();
        List<TagGetResponse> tagGetResponses = getTagGetResponses();
        when(tagRepository.getMostWidelyUsedTagOfUser()).thenReturn(tagEntities);
        when(modelMapper.map(tagEntities, new TypeToken<List<TagGetResponse>>() {
        }.getType()))
                .thenReturn(tagGetResponses);
        when(tagRepository.getCountOfMostWidelyUsedTagCount()).thenReturn(1);

        MostUsedTagResponse mostWidelyUsedTagsOfUser = tagService.getMostWidelyUsedTagsOfUser();
        assertEquals(tagGetResponses, mostWidelyUsedTagsOfUser.getTags());
        assertEquals(1, mostWidelyUsedTagsOfUser.getCount());
    }


}