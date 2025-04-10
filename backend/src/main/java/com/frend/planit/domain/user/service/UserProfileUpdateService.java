package com.frend.planit.domain.user.service;

import com.frend.planit.domain.user.dto.request.UserUpdateBioRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateEmailRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateMailingTypeRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateNicknameRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePasswordRequest;
import com.frend.planit.domain.user.dto.request.UserUpdatePhoneRequest;
import com.frend.planit.domain.user.dto.request.UserUpdateProfileImageRequest;
import com.frend.planit.domain.user.entity.User;
import com.frend.planit.domain.user.enums.LoginType;
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
public class UserProfileUpdateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateNickname(Long userId, UserUpdateNicknameRequest request) {
        if (userRepository.existsByNicknameAndIdNot(request.getNickname(), userId)) {
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

    public void updatePassword(Long userId, UserUpdatePasswordRequest request) {
        User user = getUser(userId);

        if (user.getLoginType() != LoginType.LOCAL) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID); // 소셜 로그인 사용자는 차단
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ServiceException(ErrorType.REQUEST_NOT_VALID); // 현재 비밀번호 불일치
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorType.REQUEST_NOT_VALID));
    }
}
