package com.frend.planit.domain.auth.service;

import com.frend.planit.domain.auth.dto.request.LocalRegisterRequest;
import com.frend.planit.domain.auth.dto.response.LocalRegisterResponse;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.mapper.UserMapper;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LocalRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalRegisterResponse register(LocalRegisterRequest request) {
        validateDuplication(request);

        User user = UserMapper.toEntity(request, passwordEncoder);
        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    private void validateDuplication(LocalRegisterRequest request) {
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }
    }
}
