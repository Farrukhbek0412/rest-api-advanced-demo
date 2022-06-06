package com.epam.esm.service.user;

import com.epam.esm.dto.response.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;
import com.epam.esm.exception.DataAlreadyExistException;
import com.epam.esm.exception.DataNotFoundException;
import com.epam.esm.exception.gift_certificate.InvalidCertificateException;
import com.epam.esm.exception.user.InvalidUserException;
import com.epam.esm.repository.user.UserRepository;
import com.epam.esm.service.utils.DataValidator;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final DataValidator dataValidator;

    @Override
    @Transactional
    public UserGetResponse create(UserPostRequest userPostRequest) {
        validateEmail(userPostRequest.getEmail());

        dataValidator.validateAgeNumeric(userPostRequest.getAge());
        dataValidator.isAgeOlderThan18(userPostRequest.getAge());

        UserEntity userEntity = modelMapper.map(userPostRequest, UserEntity.class);
        UserEntity saved = userRepository.create(userEntity);
        return modelMapper.map(saved, UserGetResponse.class);
    }

    void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent())
            throw new DataAlreadyExistException("This user (username = " + email + " ) already exists");
    }

    @Override
    public UserGetResponse get(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return Optional.ofNullable(modelMapper.map(userEntity.get(), UserGetResponse.class))
                .orElseThrow(() -> new DataNotFoundException("User ( id = " + id +
                        " ) not found"));

    }


    @Override
    public List<UserGetResponse> getAll(int pageSize, int pageNo) {
        if (pageNo < 1 || pageSize < 0)
            throw new InvalidCertificateException("Please enter valid number");
        List<UserEntity> userEntities = userRepository.getAll(pageSize, pageNo);
        if (userEntities.size() == 0)
            throw new DataNotFoundException("user not found");
        return modelMapper.map(userEntities, new TypeToken<List<UserGetResponse>>() {
        }.getType());
    }


}
