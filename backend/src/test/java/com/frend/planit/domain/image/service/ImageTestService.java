package com.frend.planit.domain.image.service;

import com.frend.planit.domain.image.dto.request.ImageTestRequest;
import com.frend.planit.domain.image.dto.response.ImageTestResponse;
import com.frend.planit.domain.image.entity.ImageTestEntity;
import com.frend.planit.domain.image.repository.ImageTestRepository;
import com.frend.planit.domain.image.type.HolderType;
import com.frend.planit.global.exception.ServiceException;
import com.frend.planit.global.response.ErrorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Service
public class ImageTestService {

    @Autowired
    private ImageTestRepository imageTestRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public long create(ImageTestRequest testRequest) {
        ImageTestEntity testEntity = new ImageTestEntity(
                testRequest.name(), testRequest.age(), testRequest.email());
        imageTestRepository.save(testEntity);
        imageTestRepository.flush();
        imageService.saveImages(HolderType.TEST, testEntity.id, testRequest.imageIds());

        return testEntity.id;
    }

    public ImageTestResponse read(long id) {
        ImageTestEntity testEntity = imageTestRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));

        return new ImageTestResponse(testEntity.name, testEntity.age, testEntity.email,
                imageService.getAllImages(HolderType.TEST, testEntity.id));
    }

    @Transactional
    public ImageTestResponse update(long id, ImageTestRequest testRequest) {
        ImageTestEntity testEntity = imageTestRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorType.COMMON_SERVER_ERROR));
        testEntity.name = testRequest.name();
        testEntity.age = testRequest.age();
        testEntity.email = testRequest.email();
        imageService.updateImages(HolderType.TEST, id, testRequest.imageIds());

        return new ImageTestResponse(testEntity.name, testEntity.age, testEntity.email,
                imageService.getAllImages(HolderType.TEST, id));
    }

    @Transactional
    public void delete(long id) {
        imageTestRepository.deleteById(id);
        imageService.deleteImages(HolderType.TEST, id);
    }
}