package com.frend.planit.domain.image.type;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ImageMimeType {
    JPG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    BMP("image/bmp"),
    WEBP("image/webp");

    private String value;

    ImageMimeType(String value) {
        this.value = value;
    }

    public static boolean isSupported(String contentType) {
        return Arrays.stream(values())
                .anyMatch(ImageMime -> ImageMime.value.equals(contentType));
    }
}