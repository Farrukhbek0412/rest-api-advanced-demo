package com.epam.esm.service.user;

import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.service.utils.UserServiceTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserGetResponse userGetResponse;
    private UserPostRequest userPostRequest;

    @BeforeEach
    void setUp() {
        userEntity = getUser();
        userGetResponse = getUserResponse();
        userPostRequest = getUserPostRequest();
    }

    @Test
    void createUserNonNullResultTest() {
        when(modelMapper.map(userPostRequest, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.create(userEntity)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserGetResponse.class)).thenReturn(userGetResponse);

        UserGetResponse userGetResponse = userService.create(userPostRequest);
        assertNotNull(userGetResponse);
    }

    @Test
    void getByResponseResultTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserGetResponse.class)).thenReturn(userGetResponse);

        UserGetResponse userGetResponse = userService.get(1L);
        assertEquals("email", userGetResponse.getEmail());
    }

    @Test
    public void testGetUserThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> userService.get(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllResultTest() {
        when(userRepository.getAll(10, 0)).thenReturn(List.of(userEntity));
        when(modelMapper.map(List.of(userEntity), new TypeToken<List<UserGetResponse>>() {
        }.getType())).thenReturn(List.of(userGetResponse));

        List<UserGetResponse> all = userService.getAll(10, 0);
        assertEquals(1, all.size());
    }
}