package com.frend.planit.domain.image.dto.response;

import com.frend.planit.domain.image.entity.Image;
import com.frend.planit.global.aws.s3.S3PresignedPostResponse;

public record UploadResponse(
        S3PresignedPostResponse presigned,
        long imageId,
        String getUrl
) {

    public static UploadResponse of(S3PresignedPostResponse postResponse, Image image) {
        return new UploadResponse(postResponse, image.getId(), image.getUrl());
    }
}