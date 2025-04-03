package com.frend.planit.domain.user.service;

import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.repository.UserRepository;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileUpdateService {

    private final UserRepository userRepository;

    public void updateNickname(Long userId, UserUpdateNicknameRequest request) {
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID);
        }
        getUser(userId).updateNickname(request.getNickname());
    }

    public void updateBio(Long userId, UserUpdateBioRequest request) {
        getUser(userId).updateBio(request.getBio());
    }

    public void updateProfileImage(Long userId, UserUpdateProfileImageRequest request) {
        getUser(userId).updateProfileImage(request.getProfileImageUrl());
    }

    public void updateMailingType(Long userId, UserUpdateMailingTypeRequest request) {
        getUser(userId).updateMailingType(request.isMailingType());
    }

    public void updatePhone(Long userId, UserUpdatePhoneRequest request) {
        getUser(userId).updatePhone(request.getPhone());
    }

    public void updateEmail(Long userId, UserUpdateEmailRequest request) {
        getUser(userId).updateEmail(request.getEmail());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.REQUEST_NOT_VALID));
    }
}
