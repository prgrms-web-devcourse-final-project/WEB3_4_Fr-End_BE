package com.frend.planit.domain.image.service;

import com.frend.planit.domain.image.dto.response.UploadResponse;
import com.frend.planit.domain.image.entity.Image;
import com.frend.planit.domain.image.repository.ImageRepository;
import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.domain.image.type.ImageMimeType;
import com.frend.planit.global.aws.s3.S3PresignedPostResponse;
import com.frend.planit.global.aws.s3.S3Service;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import com.frend.planit.standard.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 * 이미지 업로드는 아래의 과정으로 진행됩니다.
 * 1. 클라이언트가 서버에게 업로드용 URL(Presigned URL)을 요청
 * 2. 서버는 업로드용 URL과 미리보기 URL을 전달
 * 3. 클라이언트는 업로드용 URL로 이미지를 s3에 업로드
 * 4. 업로드가 완료되면 미리보기 URL로 이미지를 표시
 * 5. 이후 게시글을 등록할 때 실제로 사용된 이미지만 커밋
 * 6. 사용되지 않은 이미지는 스케쥴러로 삭제
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${spring.cloud.aws.s3.domain}")
    private String imageDomain;

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    public UploadResponse upload(String contentType) {
        // 이미지 확장자 검증
        if (!ImageMimeType.isSupported(contentType)) {
            throw new ServiceException(ErrorType.MIME_NOT_SUPPORTED);
        }

        // 이미지 GET URL 임시 생성
        String fileName = RandomUtil.generateUid() + '.' + contentType.split("/")[1];
        Image image = createImage(fileName);

        // Presigned POST URL 생성
        S3PresignedPostResponse postResponse = s3Service.createPresignedPostUrl(fileName,
                contentType);

        return UploadResponse.of(postResponse, image);
    }

    @Transactional
    public Image createImage(String fileName) {
        Image image = new Image();
        image.setUrl(imageDomain + fileName);
        return imageRepository.save(image);
    }

    @Transactional
    public void commitImage(long imageId, HolderType holderType, long holderId) {
        Image image = findImageById(imageId);
        image.setHolder(holderType, holderId);
        imageRepository.save(image);
    }

    public void updateImage() {
    }

    public void updateImages() {
    }

    public void deleteImage() {
    }

    public void deleteImages() {
    }

    public Image findImageById(long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ServiceException(ErrorType.IMAGE_NOT_FOUND));
    }
}