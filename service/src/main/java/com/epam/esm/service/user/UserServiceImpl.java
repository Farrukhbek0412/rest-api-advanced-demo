package com.epam.esm.service.user;

import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.user.InvalidUserException;
import com.epam.esm.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserGetResponse create(UserPostRequest userPostRequest) {
        isExist(userPostRequest.getEmail());
        isValid(userPostRequest);
        UserEntity userEntity = modelMapper.map(userPostRequest, UserEntity.class);
        UserEntity saved = userRepository.create(userEntity);
        return modelMapper.map(saved, UserGetResponse.class);
    }

    void isExist(String email){
        if(userRepository.findByEmail(email).isPresent())
            throw new DataAlreadyExistException("This user (username = " + email + " ) already exists");
    }

    void isValid(UserPostRequest userPostRequest){
        if (StringUtils.isBlank(userPostRequest.getName())) {
            throw new InvalidUserException("Enter name first!");
        }

        if (StringUtils.isBlank(userPostRequest.getEmail())) {
            throw new InvalidUserException("Enter email first!");
        }
        if (StringUtils.isBlank(userPostRequest.getPassword())) {
            throw new InvalidUserException("Enter password first!");
        }

        if (!NumberUtils.isParsable(String.valueOf(userPostRequest.getAge()))) {
            throw new InvalidUserException("The age " + userPostRequest.getAge() +
                    ", not valid,please enter only numeric age");
        }else if(Integer.parseInt(userPostRequest.getAge())<18){
            throw new InvalidUserException("User under 18 years old cannot use our system");
        }

    }

    @Override
    public UserGetResponse get(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()) {
            return modelMapper.map(userEntity.get(), UserGetResponse.class);
        }
        throw new DataNotFoundException("User ( id = " + id+" ) not found");
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<UserGetResponse> getAll(int limit, int offset) {
        List<UserEntity> userEntities = userRepository.getAll(limit, offset);
        if(userEntities.size() == 0)
            throw new DataNotFoundException("user not found");
        return modelMapper.map(userEntities, new TypeToken<List<UserGetResponse>>() {
        }.getType());
    }


}
