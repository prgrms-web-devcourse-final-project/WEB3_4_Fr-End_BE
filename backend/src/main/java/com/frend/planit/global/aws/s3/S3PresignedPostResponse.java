package com.frend.planit.global.aws.s3;

import com.fasterxml.jackson.annotation.JsonProperty;

public record S3PresignedPostResponse(
        String postUrl,
        FormData formData
) {

    public record FormData(
            String key,
            String policy,
            @JsonProperty("Content-Type") String contentType,
            @JsonProperty("x-amz-algorithm") String xAmzAlgorithm,
            @JsonProperty("x-amz-credential") String xAmzCredential,
            @JsonProperty("x-amz-date") String xAmzDate,
            @JsonProperty("x-amz-signature") String xAmzSignature
    ) {

    }
}