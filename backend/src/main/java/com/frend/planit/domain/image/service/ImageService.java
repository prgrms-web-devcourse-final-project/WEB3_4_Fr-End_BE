package com.frend.planit.domain.image.service;

import com.frend.planit.domain.image.dto.response.ImageResponse;
import com.frend.planit.domain.image.dto.response.ImagesResponse;
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
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

@Service
@Validated
@RequiredArgsConstructor
public class ImageService {

    @Value("${spring.cloud.aws.s3.domain}")
    private String imageDomain;

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    /*
     * 이미지 업로드는 아래의 과정으로 진행됩니다.
     * 1. 클라이언트가 이미지 업로드 API를 호출
     * 2. 서버는 GET URL, POST URL을 생성하여 클라이언트에게 전달
     * 3. 클라이언트는 POST URL을 통해 S3에 이미지를 업로드
     * 4. 업로드가 완료되면 클라이언트는 GET URL로 이미지에 접근
     */
    @Transactional
    public UploadResponse uploadImage(String contentType) {
        // 이미지 타입 검증 및 추출
        String imageMimeType = getImageMimeType(contentType);

        // 이미지 GET URL 임시 생성
        String fileName = RandomUtil.generateUid() + '.' + imageMimeType.split("/")[1];
        Image image = createImage(fileName);

        // Presigned POST URL 생성
        S3PresignedPostResponse postResponse = s3Service.createPresignedPostUrl(
                fileName, contentType);

        return UploadResponse.of(postResponse, image);
    }

    /*
     * 게시글을 등록할 때 최종적으로 사용된 이미지를 게시글과 연결합니다.
     * 연결되지 않은 이미지는 주기적으로 삭제됩니다.
     */
    @Transactional
    public void saveImage(
            @NonNull HolderType holderType,
            @Positive long holderId,
            @Positive long imageId) {
        int updatedCount = imageRepository.updateHolderForImage(holderType, holderId, imageId);
        if (updatedCount != 1) {
            throw new ServiceException(ErrorType.IMAGE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void saveImages(
            @NonNull HolderType holderType,
            @Positive long holderId,
            List<Long> imageIds) {
        int updatedCount = imageRepository.updateHolderForImages(holderType, holderId, imageIds);
        if (updatedCount != imageIds.size()) {
            throw new ServiceException(ErrorType.IMAGE_UPLOAD_FAILED);
        }
    }

    /*
     * 게시글에 연결된 이미지를 업로드 순으로 조회합니다.
     */
    @Transactional(readOnly = true)
    public ImageResponse getImage(@NonNull HolderType holderType, @Positive long holderId) {
        return ImageResponse.of(
                imageRepository.findFirstByHolderTypeAndHolderIdOrderByIdAsc(holderType, holderId));
    }

    @Transactional(readOnly = true)
    public ImagesResponse getImages(@NonNull HolderType holderType, @Positive long holderId) {
        return ImagesResponse.of(
                imageRepository.findAllByHolderTypeAndHolderIdOrderByIdAsc(holderType, holderId));
    }

    /*
     * 이미지 변경은 아래의 과정으로 진행됩니다.
     * 1. 변경 전 이미지의 Holder를 초기화
     * 2. 변경 후 이미지의 Holder를 설정
     */
    @Transactional
    public void updateImage(
            @NonNull HolderType holderType,
            @Positive long holderId,
            @Positive long newImageId) {
        deleteImage(holderType, holderId);
        saveImage(holderType, holderId, newImageId);
    }

    @Transactional
    public void updateImages(
            @NonNull HolderType holderType,
            @Positive long holderId,
            List<Long> newImageIds) {
        deleteImages(holderType, holderId);
        saveImages(holderType, holderId, newImageIds);
    }

    /*
     * 게시글과 이미지의 연결을 해제합니다.
     * 삭제는 주기적으로 이루어집니다.
     */
    @Transactional
    public void deleteImage(@NonNull HolderType holderType, @Positive long holderId) {
        Optional<Image> oldImage = imageRepository
                .findFirstByHolderTypeAndHolderIdOrderByIdAsc(holderType, holderId);
        if (oldImage.isEmpty()) {
            return;
        }

        int deletedCount = imageRepository.updateHolderForImage(
                null, null, oldImage.get().getId());
        if (deletedCount != 1) {
            throw new ServiceException(ErrorType.IMAGE_DELETE_FAILED);
        }
    }

    @Transactional
    public void deleteImages(@NonNull HolderType holderType, @Positive long holderId) {
        List<Image> oldImages = imageRepository.findAllByHolderTypeAndHolderIdOrderByIdAsc(
                holderType, holderId);
        if (oldImages.isEmpty()) {
            return;
        }

        int deletedCount = imageRepository.updateHolderForImages(
                null, null, oldImages.stream().map(Image::getId).toList());
        if (deletedCount != oldImages.size()) {
            throw new ServiceException(ErrorType.IMAGE_DELETE_FAILED);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public long cleanImages() {
        try {
            List<Image> unusedImages = imageRepository.findAllByHolderTypeIsNullAndHolderIdIsNull();
            if (unusedImages.isEmpty()) {
                return 0;
            }
            List<ObjectIdentifier> imageNames = unusedImages.stream()
                    .map(image -> ObjectIdentifier.builder().key(image.getFileName()).build())
                    .toList();

            imageRepository.deleteAllInBatch(unusedImages);
            s3Service.deleteFiles(imageNames);
            return unusedImages.size();
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorType.IMAGE_CLEAN_FAILED, e);
        }
    }

    private String getImageMimeType(String contentType) {
        for (String element : contentType.split(";")) {
            element = element.trim();
            if (ImageMimeType.isSupported(element)) {
                return element;
            }
        }

        throw new ServiceException(ErrorType.MIME_NOT_SUPPORTED);
    }

    @Transactional
    private Image createImage(String fileName) {
        Image image = new Image();
        image.setUrl(imageDomain + fileName);
        image.setFileName(fileName);
        return imageRepository.save(image);
    }
}